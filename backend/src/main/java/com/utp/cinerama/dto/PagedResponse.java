package com.utp.cinerama.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para respuestas paginadas
 * Contiene la lista de elementos y metadata de paginacion
 * 
 * Uso en Angular:
 * - content: datos a mostrar
 * - pageNumber: pagina actual (0-indexed)
 * - pageSize: elementos por pagina
 * - totalElements: total de elementos en BD
 * - totalPages: total de paginas
 * - first/last: flags para deshabilitar botones
 * 
 * @param <T> Tipo de dato en la lista
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    
    /**
     * Lista de elementos de la pagina actual
     */
    private List<T> content;
    
    /**
     * Numero de pagina actual (empieza en 0)
     */
    private int pageNumber;
    
    /**
     * Cantidad de elementos por pagina
     */
    private int pageSize;
    
    /**
     * Total de elementos en toda la BD
     */
    private long totalElements;
    
    /**
     * Total de paginas disponibles
     */
    private int totalPages;
    
    /**
     * Indica si es la primera pagina
     * Util para deshabilitar boton "anterior"
     */
    private boolean first;
    
    /**
     * Indica si es la ultima pagina
     * Util para deshabilitar boton "siguiente"
     */
    private boolean last;
    
    /**
     * Indica si hay pagina siguiente
     */
    private boolean hasNext;
    
    /**
     * Indica si hay pagina anterior
     */
    private boolean hasPrevious;
}

