package com.utp.cinerama.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración de Spring Security
 * Define reglas de autorización, CORS, CSRF, y filtros JWT
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    // Inyectar configuraciones de CORS desde application.properties
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    @Value("${cors.allowed-methods}")
    private String[] allowedMethods;

    @Value("${cors.allowed-headers}")
    private String allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

    @Value("${cors.max-age}")
    private long maxAge; 

    /**
     * Configuración principal de seguridad
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Configurar autorización de requests
                .authorizeHttpRequests(auth -> auth
                        // ========== RUTAS PÚBLICAS (sin autenticación) ==========

                        // Ruta raíz
                        .requestMatchers("/").permitAll()
                        
                        // Recursos estáticos (solo si existen)
                        .requestMatchers("/favicon.ico").permitAll()

                        // Autenticación
                        .requestMatchers("/api/auth/**").permitAll()
                        
                        // Películas (consulta pública)
                        .requestMatchers(HttpMethod.GET, "/api/peliculas/**").permitAll()
                        
                        // TMDb (exploración pública, agregar requiere admin)
                        .requestMatchers(HttpMethod.GET, "/api/tmdb/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/api/salas/**").permitAll()
                        
                        
                        .requestMatchers(HttpMethod.GET, "/api/funciones/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/funciones/generar-automaticas").hasRole("ADMIN")
                        
                        
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        
                        
                        .requestMatchers(HttpMethod.GET, "/api/asientos/funcion/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/asientos/estadisticas/**").permitAll()
                        
                        // WebSocket endpoint
                        .requestMatchers("/ws/**").permitAll()

                        // ========== RUTAS SOLO ADMIN ==========
                        
                        // Gestión de películas
                        .requestMatchers(HttpMethod.POST, "/api/peliculas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/peliculas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/peliculas/**").hasRole("ADMIN")
                        
                        // Agregar película desde TMDb (solo admin)
                        .requestMatchers(HttpMethod.POST, "/api/peliculas/agregar-desde-tmdb").hasRole("ADMIN")
                        
                        // Gestión de salas
                        .requestMatchers(HttpMethod.POST, "/api/salas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/salas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/salas/**").hasRole("ADMIN")
                        
                        // Gestión de funciones
                        .requestMatchers(HttpMethod.POST, "/api/funciones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/funciones/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/funciones/**").hasRole("ADMIN")
                        
                        // Gestión de productos
                        .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                        
                        // Gestión de usuarios
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        
                        // Generación de asientos
                        .requestMatchers(HttpMethod.POST, "/api/asientos/generar/**").hasRole("ADMIN")

                        // ========== RUTAS AUTENTICADAS (cualquier usuario logueado) ==========
                        
                        // Compra de boletos
                        .requestMatchers("/api/boletos/**").authenticated()
                        
                        // Orquestador de compras (requiere autenticación)
                        .requestMatchers("/api/compras/**").authenticated()
                        
                        // Reserva de asientos
                        .requestMatchers(HttpMethod.POST, "/api/asientos/reservar/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/asientos/confirmar/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/asientos/liberar/**").authenticated()
                        
                        // Compra de productos
                        .requestMatchers("/api/ventas-producto/**").authenticated()
                        
                        // Pagos
                        .requestMatchers("/api/pagos/**").authenticated()
                        
                        // Clientes (solo sus propios datos)
                        .requestMatchers("/api/clientes/**").authenticated()

                        // ========== CUALQUIER OTRA RUTA REQUIERE AUTENTICACIÓN ==========
                        .anyRequest().authenticated()
                )

                // Configurar gestión de sesiones (STATELESS para JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Agregar filtro JWT antes del filtro de autenticación estándar
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuración de CORS SEGURA para permitir peticiones desde el frontend
     * Lee configuraciones desde application.properties para facilitar cambios
     * entre entornos (dev/prod)
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("Configurando CORS con origenes permitidos: {}", Arrays.toString(allowedOrigins));
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Origenes permitidos (desde properties)
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        
        // Métodos HTTP permitidos (desde properties)
        configuration.setAllowedMethods(Arrays.asList(allowedMethods));
        
        // Headers permitidos
        if ("*".equals(allowedHeaders)) {
            configuration.setAllowedHeaders(List.of("*"));
        } else {
            configuration.setAllowedHeaders(Arrays.asList(allowedHeaders.split(",")));
        }
        
        // Headers expuestos al cliente
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count",
                "X-Total-Pages"
        ));
        
        // Permitir credenciales (cookies, authorization headers)
        configuration.setAllowCredentials(allowCredentials);
        
        // Tiempo de cache para preflight requests (en segundos)
        configuration.setMaxAge(maxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        log.info("CORS configurado exitosamente");
        return source;
    }

    /**
     * Bean para encriptar contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

