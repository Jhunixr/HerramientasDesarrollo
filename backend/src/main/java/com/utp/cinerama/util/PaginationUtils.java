package com.utp.cinerama.util;

import com.utp.cinerama.dto.PagedResponse;
import org.springframework.data.domain.Page;

/**
 * Utilidad para convertir Page de Spring a PagedResponse personalizado
 * 
 * ¿Por que?
 * - Spring devuelve Page<T> con mucha info que Angular no necesita
 * - PagedResponse<T> tiene solo lo necesario para la UI
 * - Mas facil de usar en Angular
 */
public class PaginationUtils {

    /**
     * Convierte Page<T> de Spring a PagedResponse<T>
     * 
     * @param page Page de Spring Data
     * @return PagedResponse para Angular
     */
    public static <T> PagedResponse<T> toPagedResponse(Page<T> page) {
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}

