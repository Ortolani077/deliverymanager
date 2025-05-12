package com.example.gerenciamento.Controllers;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.gerenciamento.DTOs.AuthRequest;
import com.example.gerenciamento.DTOs.AuthResponse;
import com.example.gerenciamento.DTOs.UserRegistrationRequest;
import com.example.gerenciamento.Security.CustomUserDetails;
import com.example.gerenciamento.Security.JwtTokenProvider;
import com.example.gerenciamento.Services.CustomUserDetailsService;
import com.example.gerenciamento.Services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, 
                          CustomUserDetailsService userDetailsService, 
                          JwtTokenProvider jwtTokenProvider,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    // Método de login
    @PostMapping("/login")
    public ModelAndView login(@RequestBody AuthRequest authRequest, HttpServletResponse response) {
        // Autentica o usuário usando os dados fornecidos
        UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // Gerar o token JWT
        String token = jwtTokenProvider.generateToken(userDetails.getUsername(), List.of("ROLE_USER"));

        // Logando o token gerado
        System.out.println("JWT Token: " + token);  // Aqui você está imprimindo o token no console

        // Criar o cookie HttpOnly com o token JWT
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);  // Protege o cookie contra ataques XSS
        cookie.setSecure(false);  // Use como `true` apenas em ambiente HTTPS
        cookie.setPath("/");  // Defina o caminho adequado para o cookie
        cookie.setMaxAge(3600);  // 1 hora de validade do cookie

        // Adicionando o cookie na resposta
        response.addCookie(cookie);

        // Redirecionando para a página de início após autenticação bem-sucedida
        // Se preferir enviar uma resposta JSON, substitua `ModelAndView` por `ResponseEntity` ou use um `Map` com o token.
        ModelAndView modelAndView = new ModelAndView("redirect:/inicio.html");
        modelAndView.addObject("token", token);  // Incluindo o token no modelo, se necessário
        return modelAndView;
    }

    // Método de registro
    @PostMapping("/register")
    public AuthResponse register(@RequestBody UserRegistrationRequest request) {
        System.out.println("Recebendo requisição de registro para usuário: " + request.getUsername());

        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username já está em uso!");
        }

        AuthResponse response = userService.registerUser(request);
        System.out.println("Usuário registrado com sucesso: " + request.getUsername());

        return response;
    }

    // Método de logout
    @PostMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            // Logout da autenticação
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // Invalidar a sessão
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Remover o cookie JWT
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token")) {
                    cookie.setMaxAge(0);  // Expira o cookie imediatamente
                    cookie.setPath("/");  // Define o caminho para garantir a remoção
                    response.addCookie(cookie);
                }
            }
        }

        // Redirecionar para a página de login
        return new ModelAndView("redirect:/index.html");
    }
}
