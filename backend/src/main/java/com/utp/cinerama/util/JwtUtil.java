package com.utp.cinerama.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Utilidad para generar y validar tokens JWT
 * Patron simplificado siguiendo estandares de la industria
 */
@Component
@Slf4j
public class JwtUtil {

    // Clave secreta fija de 64 caracteres (Base64) para entorno academico
    // Nota: En produccion deberia ir en un vault o variable de entorno
    private final String SECRET_KEY = "NDA0RTYzNTI2NjU1NkE1ODZFMzI3MjM1NzUzODc4MkY0MTNGNDQyODQ3MkI0QjYyNTA2NDUzNjc1NjZCNTk3MA==";

    /**
     * Obtener la clave de firma desde el secret (Base64)
     * Este metodo centraliza la obtencion de la clave para evitar repeticion de codigo
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Compatibilidad con ejemplo simplificado:
     * Genera un token con un unico rol en el claim "rol" y ademas en "roles" (lista)
     * Expiracion fija de 1 hora si no se configuro por propiedades.
     */
    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);
        // Tambien mantener consistencia con el resto de la app
        claims.put("roles", Collections.singletonList(rol));

    Date now = new Date();
    // Expiracion fija de 1 hora como en el ejemplo simplificado
    Date expiryDate = new Date(now.getTime() + (1000L * 60 * 60));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extraer todos los claims (datos) del token
     * Este metodo es la base para extraer informacion especifica
     */
    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.error("Token expirado: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            log.error("Token malformado: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Token no soportado: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string vacio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error al extraer claims: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Extraer username del token
     * El username esta almacenado en el "subject" del JWT
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Compatibilidad con ejemplo simplificado: extrae el claim "rol" simple
     */
    public String extractRol(String token) {
        Object value = extractAllClaims(token).get("rol");
        return value != null ? value.toString() : null;
    }

    /**
     * Verificar si el token ha expirado
     * Compara la fecha de expiracion con la fecha actual
     */
    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Token expirado
        } catch (Exception e) {
            log.error("Error al verificar expiracion: {}", e.getMessage());
            return true; // Por seguridad, considerarlo expirado
        }
    }

    /**
     * Validar token completamente
     * Verifica que el token sea valido y no haya expirado
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token); // Intenta parsear el token
            return !isTokenExpired(token); // Verifica que no haya expirado
        } catch (Exception e) {
            log.error("Token invalido: {}", e.getMessage());
            return false;
        }
    }

}

