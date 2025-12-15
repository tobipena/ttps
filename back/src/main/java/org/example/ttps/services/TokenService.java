package org.example.ttps.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.ttps.models.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenService {

    private final SecretKey key;

    public TokenService(@Value("${security.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Usuario u, int expirationMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date exp = new Date(nowMillis + expirationMillis);

        return Jwts.builder()
                .subject(u.getEmail())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = parseClaims(token);
            return !isExpired(claims);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token) {
        String prefix = "Bearer ";
        if (token.startsWith(prefix)) {
            token = token.substring(prefix.length()).trim();
        }
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isExpired(Claims claims) {
        Date exp = claims.getExpiration();
        return exp != null && exp.before(new Date());
    }
}
