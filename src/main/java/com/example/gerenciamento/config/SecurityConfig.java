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

import com.example.gerenciamento.Security.JwtAuthenticationFilter;
import com.example.gerenciamento.Security.JwtTokenProvider;
import com.example.gerenciamento.Services.CustomUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;

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
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                                // Permite o acesso público para páginas como login e estáticas
                                .requestMatchers("/index.html", "/static/**", "/api/auth/**", "/api/test/**", "/inicio.html").permitAll()

                                // Bloqueia o acesso à página de relatório para usuários com ROLE_USER
                                .requestMatchers("/relatorio.html").hasRole("ADMIN")  // Apenas usuários com ROLE_ADMIN podem acessar

                                // Permite DELETE para qualquer usuário autenticado
                                .requestMatchers(HttpMethod.DELETE, "/**").authenticated()

                                // Requer autenticação para todas as outras requisições
                                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                                .loginPage("/index.html")  // Página de login
                                .loginProcessingUrl("/api/auth/login")  // URL de login
                                .defaultSuccessUrl("/inicio.html", true)  // Página após login bem-sucedido
                                .failureUrl("/index.html?error=true")  // Página em caso de falha no login
                                .permitAll()
                )
                .logout(logout -> logout
                                .logoutUrl("/api/auth/logout")  // URL para logout
                                .logoutSuccessUrl("/index.html")  // Página após logout bem-sucedido
                                .invalidateHttpSession(true)  // Invalida a sessão após o logout
                                .deleteCookies("JSESSIONID")  // Exclui o cookie de sessão
                                .permitAll()
                )
                .sessionManagement(session -> session
                                .sessionFixation().newSession()  // Cria uma nova sessão após login
                                .maximumSessions(1)  // Permite uma única sessão por usuário
                                .expiredUrl("/index.html?expired=true")  // Página em caso de sessão expirada
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptions -> exceptions
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Você precisa estar autenticado para acessar este recurso.");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Você não tem permissão para acessar este recurso.");
                                })
                );

        return http.build();
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
