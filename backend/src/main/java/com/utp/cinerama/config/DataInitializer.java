package com.utp.cinerama.config;

import com.utp.cinerama.model.Rol;
import com.utp.cinerama.model.Sala;
import com.utp.cinerama.model.Usuario;
import com.utp.cinerama.repository.RolRepository;
import com.utp.cinerama.repository.SalaRepository;
import com.utp.cinerama.repository.UsuarioRepository;
import com.utp.cinerama.service.FuncionService;
import com.utp.cinerama.service.PeliculaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final SalaRepository salaRepository;
    private final FuncionService funcionService;
    private final PeliculaService peliculaService;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    CommandLineRunner initData() {
        return args -> {
            try {
                log.info("🚀 Inicializando datos del sistema...");
                
                // 0. Crear roles por defecto si no existen
                Rol rolAdmin = rolRepository.findByNombre("ROLE_ADMIN")
                        .orElseGet(() -> {
                            log.info("👥 Creando rol ADMIN...");
                            Rol rol = Rol.builder()
                                    .nombre("ROLE_ADMIN")
                                    .descripcion("Administrador del sistema")
                                    .build();
                            return rolRepository.save(rol);
                        });
                
                Rol rolCliente = rolRepository.findByNombre("ROLE_CLIENTE")
                        .orElseGet(() -> {
                            log.info("👥 Creando rol CLIENTE...");
                            Rol rol = Rol.builder()
                                    .nombre("ROLE_CLIENTE")
                                    .descripcion("Cliente del cine")
                                    .build();
                            return rolRepository.save(rol);
                        });
                
                // 0.1. Crear usuario administrador por defecto si no existe
                if (!usuarioRepository.existsByUsername("admin")) {
                    log.info("👤 Creando usuario administrador por defecto...");
                    Usuario admin = Usuario.builder()
                            .username("admin")
                            .email("admin@cinerama.com")
                            .password(passwordEncoder.encode("admin123"))
                            .activo(true)
                            .cuentaNoExpirada(true)
                            .cuentaNoBloqueada(true)
                            .credencialesNoExpiradas(true)
                            .build();
                    
                    admin.agregarRol(rolAdmin);
                    usuarioRepository.save(admin);
                    log.info("✅ Usuario administrador creado:");
                    log.info("   Username: admin");
                    log.info("   Password: admin123");
                    log.info("   Email: admin@cinerama.com");
                } else {
                    log.info("ℹ️  Usuario administrador ya existe");
                }
                
                // 1. Crear salas por defecto si no existen
                if (salaRepository.count() == 0) {
                    log.info("📽️ Creando salas por defecto...");
                    Sala sala1 = Sala.builder()
                            .nombre("Sala 1")
                            .descripcion("Sala principal con sonido envolvente")
                            .capacidad(100)
                            .tipo(Sala.TipoSala.NORMAL)
                            .activa(true)
                            .build();
                    
                    Sala sala2 = Sala.builder()
                            .nombre("Sala 2")
                            .descripcion("Sala VIP con asientos reclinables")
                            .capacidad(80)
                            .tipo(Sala.TipoSala.CINE_2D)
                            .activa(true)
                            .build();
                    
                    Sala sala3 = Sala.builder()
                            .nombre("Sala 3")
                            .descripcion("Sala estándar")
                            .capacidad(120)
                            .tipo(Sala.TipoSala.NORMAL)
                            .activa(true)
                            .build();
                    
                    salaRepository.save(sala1);
                    salaRepository.save(sala2);
                    salaRepository.save(sala3);
                    log.info("✅ Salas creadas exitosamente");
                }
                
                // 2. Sincronizar películas desde TMDB API
                log.info("🎬 Sincronizando películas desde TMDB API...");
                try {
                    var resultado = peliculaService.sincronizarPeliculasDesdeAPI(2); // Sincronizar 2 páginas
                    log.info("✅ Películas sincronizadas: {} nuevas, {} actualizadas", 
                        resultado.getPeliculasNuevas(), resultado.getPeliculasActualizadas());
                } catch (Exception e) {
                    log.error("❌ Error al sincronizar películas desde TMDb: {}", e.getMessage(), e);
                }
                
                // 3. Generar funciones automáticamente para películas en cartelera
                log.info("🎫 Generando funciones automáticamente...");
                try {
                    var funciones = funcionService.generarFuncionesParaPeliculasEnCartelera();
                    log.info("✅ Funciones generadas: {}", funciones.size());
                } catch (Exception e) {
                    log.warn("⚠️ No se pudieron generar funciones automáticamente: {}", e.getMessage());
                }
                
                log.info("✨ Inicialización completada");
            } catch (Exception e) {
                log.error("❌ ERROR CRÍTICO en la inicialización de datos: {}", e.getMessage(), e);
                throw e; // Re-lanzar para que Spring Boot falle si hay un error crítico
            }
        };
    }
}

