package com.example.gerenciamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.gerenciamento.Security.JwtAuthenticationFilter;
import com.example.gerenciamento.Security.JwtTokenProvider;
import com.example.gerenciamento.Services.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))  // Ativa e configura CORS
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers(
                    "/index.html",
                    "/",
                    "/static/**",
                    "/css/**",
                    "/js/**",
                    "/images/**",
                    "/api/auth/**",
                    "/favicon.ico",
                    "/erro-acesso.html"
                ).permitAll()

                // API de teste - requer autenticação
                .requestMatchers("/api/test/**").authenticated()

                // Página inicial - requer autenticação
                .requestMatchers("/inicio.html").authenticated()

                // Relatórios - apenas para ADMIN
                .requestMatchers("/relatorio.html", "/api/relatorios/**").hasRole("ADMIN")

                // Operações DELETE
                .requestMatchers(HttpMethod.DELETE, "/**").permitAll()

                // Todas as outras requisições
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/index.html")
                .loginProcessingUrl("/api/auth/login")
                .defaultSuccessUrl("/inicio.html", true)
                .failureUrl("/index.html?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessUrl("/index.html?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            .sessionManagement(session -> session
                .sessionFixation().newSession()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
                .expiredUrl("/index.html?expired=true")
            )
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint((request, response, authException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado");
                    } else {
                        response.sendRedirect("/index.html?unauthorized=true");
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    if (request.getRequestURI().startsWith("/api/")) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
                    } else {
                        if (request.getRequestURI().equals("/relatorio.html") ||
                            request.getRequestURI().startsWith("/api/relatorios/")) {
                            response.sendRedirect("/erro-acesso.html");
                        } else {
                            response.sendRedirect("/index.html?forbidden=true");
                        }
                    }
                })
            )
            .headers(headers -> headers
                .frameOptions().sameOrigin()
                .httpStrictTransportSecurity().disable()
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            "http://localhost:8082",
            "http://localhost:8080",
            "https://deliverymanager.onrender.com"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        // Caso queira expor headers no CORS, use:
        // configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Usando BCrypt para codificação de senha
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }
}
