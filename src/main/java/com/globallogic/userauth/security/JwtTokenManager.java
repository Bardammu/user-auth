package com.globallogic.userauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Component that manage JWT tokens
 *
 * @since 1.0.0
 */
@Component
public class JwtTokenManager {

    private final String base64EncodedSecretKey;

    private final int tokenExpirationMinutes;


    public JwtTokenManager(@Value("${userauth.jwt.base64EncodedSecretKey}") String base64EncodedSecretKey,
                           @Value("${userauth.jwt.expirationMinutes}") int tokenExpirationMinutes) {
        this.base64EncodedSecretKey = base64EncodedSecretKey;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
    }

    public String generateJwtToken(String email) {
        LocalDateTime createdLocalDateTime = LocalDateTime.now();
        LocalDateTime expirationLocalDateTime = createdLocalDateTime.plusMinutes(tokenExpirationMinutes);
        Date createdDate = Date.from(createdLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());
        Date expirationDate =  Date.from(expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

        final byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        final Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        final byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        final Key key = Keys.hmacShaKeyFor(keyBytes);
        final Claims claims  = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }
}
