package com.globallogic.userauth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.apache.logging.log4j.LogManager.getLogger;

/**
 * Component that manage JWT tokens
 *
 * @since 1.0.0
 */
@Component
public class JwtTokenManager {

    private final Logger logger = getLogger(JwtTokenManager.class);

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

        final Key key = getKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            final Key key = getKey(base64EncodedSecretKey);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("JWT token expired: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("Token is null, empty or only whitespace: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            logger.error("JWT token is invalid: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT token is not supported: {}", ex.getMessage());
        }

        return false;
    }

    public String getEmailFromToken(String token) {
        final Key key = getKey(base64EncodedSecretKey);
        final Claims claims  = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    private Key getKey(String base64EncodedSecretKey) {
        final byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
