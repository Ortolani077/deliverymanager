package com.example.gerenciamento.Security;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private final long EXPIRATION_TIME = 86400000; // 1 dia em milissegundos

    // Geração do token com username e roles
    public String generateToken(String username, List<String> roles) {
        String token = Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS512) // Usando a chave secreta para assinatura
                .compact();

        // Imprime o token no console
        System.out.println("Token JWT gerado: " + token);
        
        return token;
    }

    // Resolver o token a partir do header Authorization
    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // Remove o prefixo "Bearer "
        }
        return null;
    }

    // Validar o token JWT
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())) // Configura a chave para validação
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Verifica se o token ainda não expirou
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            System.out.println("Erro ao validar o token: " + e.getMessage());
            return false;
        }
    }

    // Obter o username a partir do token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())) // A chave secreta
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject(); // Retorna o username do token
    }

    // Obter as roles a partir do token
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())) // A chave secreta
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class); // Retorna as roles armazenadas no token
    }
}
