package com.utp.cinerama.service.impl;

import com.utp.cinerama.dto.*;
import com.utp.cinerama.model.*;
import com.utp.cinerama.repository.*;
import com.utp.cinerama.service.AsientoService;
import com.utp.cinerama.service.CompraService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompraServiceImpl implements CompraService {

    private final FuncionRepository funcionRepository;
    private final AsientoRepository asientoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final BoletoRepository boletoRepository;
    private final PagoRepository pagoRepository;
    private final VentaProductoRepository ventaProductoRepository;
    private final DetalleVentaProductoRepository detalleVentaProductoRepository;
    private final AsientoService asientoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional(readOnly = true)
    public CalcularTotalResponseDTO calcularTotal(CalcularTotalRequestDTO request, Long clienteId) {
        log.info("Calculando total para función {} con {} asientos y {} productos", 
            request.getFuncionId(), request.getAsientoIds().size(), 
            request.getProductos() != null ? request.getProductos().size() : 0);

        // 1. Obtener función
        Funcion funcion = funcionRepository.findById(request.getFuncionId())
            .orElseThrow(() -> new IllegalArgumentException("Función no encontrada"));

        // 2. Calcular subtotal de boletos
        double subtotalBoletos = 0.0;
        List<BoletoItemDTO> boletos = new ArrayList<>();
        
        for (Long asientoId : request.getAsientoIds()) {
            Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento no encontrado: " + asientoId));
            
            // Verificar que el asiento pertenece a la función
            if (!asiento.getFuncion().getId().equals(funcion.getId())) {
                throw new IllegalArgumentException("El asiento no pertenece a esta función");
            }
            
            double precio = funcion.getPrecio() * asiento.getTipo().getMultiplicadorPrecio();
            subtotalBoletos += precio;
            
            boletos.add(BoletoItemDTO.builder()
                .asientoId(asientoId)
                .codigoAsiento(asiento.getCodigoAsiento())
                .precio(precio)
                .build());
        }

        // 3. Calcular subtotal de productos
        double subtotalProductos = 0.0;
        List<ProductoDetalleDTO> productosDetalle = new ArrayList<>();
        
        if (request.getProductos() != null && !request.getProductos().isEmpty()) {
            for (ProductoItemDTO item : request.getProductos()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.getProductoId()));
                
                if (!producto.getActivo()) {
                    throw new IllegalStateException("El producto " + producto.getNombre() + " no está disponible");
                }
                
                if (producto.getStock() < item.getCantidad()) {
                    throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
                }
                
                double subtotal = producto.getPrecio() * item.getCantidad();
                subtotalProductos += subtotal;
                
                productosDetalle.add(ProductoDetalleDTO.builder()
                    .productoId(producto.getId())
                    .nombre(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build());
            }
        }

        // 4. Calcular total
        double total = subtotalBoletos + subtotalProductos;

        return CalcularTotalResponseDTO.builder()
            .subtotalBoletos(subtotalBoletos)
            .subtotalProductos(subtotalProductos)
            .total(total)
            .boletos(boletos)
            .productos(productosDetalle)
            .build();
    }

    @Override
    @Transactional
    public ConfirmarCompraResponseDTO confirmarCompra(ConfirmarCompraRequestDTO request, Long clienteId) {
        log.info("Confirmando compra para cliente {} en función {}", clienteId, request.getFuncionId());

        // 1. Obtener cliente
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        // 2. Obtener función
        Funcion funcion = funcionRepository.findById(request.getFuncionId())
            .orElseThrow(() -> new IllegalArgumentException("Función no encontrada"));

        // 3. Validar y confirmar asientos
        List<BoletoResumenDTO> boletosResumen = new ArrayList<>();
        double totalBoletos = 0.0;
        
        for (Long asientoId : request.getAsientoIds()) {
            Asiento asiento = asientoRepository.findById(asientoId)
                .orElseThrow(() -> new IllegalArgumentException("Asiento no encontrado: " + asientoId));
            
            // Verificar que el asiento pertenece a la función
            if (!asiento.getFuncion().getId().equals(funcion.getId())) {
                throw new IllegalArgumentException("El asiento no pertenece a esta función");
            }
            
            // Confirmar reserva del asiento
            asientoService.confirmarReserva(asientoId);
            
            // Notificar vía WebSocket que el asiento fue confirmado
            AsientoUpdateDTO updateDTO = AsientoUpdateDTO.builder()
                .asientoId(asientoId)
                .funcionId(funcion.getId())
                .estado(Asiento.EstadoAsiento.OCUPADO)
                .accion("confirmar")
                .build();
            messagingTemplate.convertAndSend(
                "/topic/asientos/funcion/" + funcion.getId(),
                updateDTO
            );
            
            // Crear boleto
            double precio = funcion.getPrecio() * asiento.getTipo().getMultiplicadorPrecio();
            Boleto boleto = Boleto.builder()
                .cliente(cliente)
                .funcion(funcion)
                .asiento(asiento)
                .precio(precio)
                .estado(Boleto.EstadoBoleto.PAGADO)
                .fechaCompra(LocalDateTime.now())
                .build();
            
            Boleto boletoGuardado = boletoRepository.save(boleto);
            totalBoletos += precio;
            
            boletosResumen.add(BoletoResumenDTO.builder()
                .boletoId(boletoGuardado.getId())
                .codigoAsiento(asiento.getCodigoAsiento())
                .precio(precio)
                .build());
        }

        // 4. Procesar productos
        List<ProductoDetalleDTO> productosDetalle = new ArrayList<>();
        double totalProductos = 0.0;
        VentaProducto ventaProducto = null;
        
        if (request.getProductos() != null && !request.getProductos().isEmpty()) {
            // Crear venta de productos
            ventaProducto = new VentaProducto();
            ventaProducto.setCliente(cliente);
            ventaProducto.setMetodoPago(request.getMetodoPago());
            ventaProducto.setCompletada(true);
            ventaProducto = ventaProductoRepository.save(ventaProducto);
            
            for (ProductoItemDTO item : request.getProductos()) {
                Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.getProductoId()));
                
                if (!producto.getActivo()) {
                    throw new IllegalStateException("El producto " + producto.getNombre() + " no está disponible");
                }
                
                if (producto.getStock() < item.getCantidad()) {
                    throw new IllegalStateException("Stock insuficiente para " + producto.getNombre());
                }
                
                // Actualizar stock
                producto.setStock(producto.getStock() - item.getCantidad());
                productoRepository.save(producto);
                
                // Crear detalle de venta
                DetalleVentaProducto detalle = new DetalleVentaProducto();
                detalle.setVentaProducto(ventaProducto);
                detalle.setProducto(producto);
                detalle.setCantidad(item.getCantidad());
                detalleVentaProductoRepository.save(detalle);
                
                double subtotal = producto.getPrecio() * item.getCantidad();
                totalProductos += subtotal;
                
                productosDetalle.add(ProductoDetalleDTO.builder()
                    .productoId(producto.getId())
                    .nombre(producto.getNombre())
                    .cantidad(item.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .subtotal(subtotal)
                    .build());
            }
        }

        // 5. Crear pago
        double total = totalBoletos + totalProductos;
        Pago pago = Pago.builder()
            .cliente(cliente)
            .monto(total)
            .metodoPago(request.getMetodoPago())
            .tipoComprobante(request.getTipoComprobante())
            .estado(Pago.EstadoPago.COMPLETADO)
            .build();
        pagoRepository.save(pago);

        // 6. Generar número de confirmación único
        String numeroConfirmacion = "CIN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 7. Construir respuesta
        return ConfirmarCompraResponseDTO.builder()
            .numeroConfirmacion(numeroConfirmacion)
            .fechaCompra(LocalDateTime.now())
            .total(total)
            .metodoPago(request.getMetodoPago())
            .tipoComprobante(request.getTipoComprobante())
            .boletos(boletosResumen)
            .productos(productosDetalle)
            .pelicula(PeliculaResumenDTO.builder()
                .id(funcion.getPelicula().getId())
                .titulo(funcion.getPelicula().getTitulo())
                .posterUrl(funcion.getPelicula().getPosterUrl())
                .build())
            .funcion(FuncionResumenDTO.builder()
                .id(funcion.getId())
                .fechaHora(funcion.getFechaHora())
                .salaNombre(funcion.getSala().getNombre())
                .build())
            .build();
    }
}

