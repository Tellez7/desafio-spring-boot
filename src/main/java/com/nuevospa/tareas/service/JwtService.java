package com.nuevospa.tareas.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    /**
     * Clave secreta leída de application.yml (Base64)
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tiempo de expiración en milisegundos (application.yml)
     */
    @Value("${jwt.expiration}")
    private long expirationMillis;

    /* ------------------------------------------------------------------------
       API PÚBLICA
       --------------------------------------------------------------------- */

    /**
     * Genera un JWT con claims opcionales
     */
    public String generateToken(UserDetails user, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(expirationMillis)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Genera un JWT sin claims extra
     */
    public String generateToken(UserDetails user) {
        return generateToken(user, Map.of());
    }

    /**
     * Devuelve true si el token es válido para ese usuario
     */
    public boolean isTokenValid(String token, UserDetails user) {
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extrae el username (subject) del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /* ------------------------------------------------------------------------
       MÉTODOS PRIVADOS
       --------------------------------------------------------------------- */

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    /**
     * Convierte la clave Base64 a Key
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
