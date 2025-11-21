package com.utp.cinerama.config;

import com.utp.cinerama.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extraer token del header Authorization o de cookies
        String jwt = extractJwtFromRequest(request);
        String usernameOrEmail = null;

        // 2. Validar y extraer username del token
        if (jwt != null) {
            try {
                usernameOrEmail = jwtUtil.extractUsername(jwt);
                log.debug("🔑 Token JWT detectado para usuario: {}", usernameOrEmail);
            } catch (Exception e) {
                log.error("❌ Error al extraer username del token: {}", e.getMessage());
            }
        }

        // 3. Si hay username y no está autenticado en el contexto
        if (usernameOrEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            try {
                // 4. Cargar detalles del usuario
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameOrEmail);

                // 5. Validar token (firma + expiración)
                if (!jwtUtil.isTokenExpired(jwt)) {
                    log.info("✅ Token válido para usuario: {}", usernameOrEmail);

                    // 6. Crear objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    // 7. Establecer detalles de la petición (IP, session, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 8. Establecer autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("🔐 Usuario autenticado: {} con roles: {}", 
                        usernameOrEmail, 
                        userDetails.getAuthorities()
                    );
                } else {
                    log.warn("⚠️ Token expirado para usuario: {}", usernameOrEmail);
                }
            } catch (Exception e) {
                log.error("❌ Error al autenticar usuario: {}", e.getMessage());
            }
        }

        // 9. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extraer JWT del header Authorization o de las cookies
     * 
     * PRIORIDAD:
     * 1. Header Authorization: Bearer <token>
     * 2. Cookie: jwt=<token>
     * 
     * USO:
     * - Header: Para aplicaciones SPA (React, Angular, Vue)
     * - Cookie: Para remember me y seguridad adicional (HttpOnly)
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        // 1. Intentar obtener del header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Remover "Bearer "
        }

        // 2. Intentar obtener de las cookies (para remember me)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt".equals(cookie.getName())) {
                    log.debug("🍪 JWT encontrado en cookie");
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    /**
     * No filtrar rutas públicas (login, register, etc.)
     * 
     * OPTIMIZACIÓN: Evita ejecutar el filtro en rutas que no requieren autenticación
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/login") || 
               path.startsWith("/api/auth/register") ||
               path.startsWith("/api/auth/validar-username") ||
               path.startsWith("/api/auth/validar-email") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs");
    }
}

