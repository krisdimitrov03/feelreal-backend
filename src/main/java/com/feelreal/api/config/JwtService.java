package com.feelreal.api.config;

import com.feelreal.api.dto.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "WVAI2eAg1ranDyur6gRNkxeVoD5UHQUAwHawujjtC9XNBkyFooUpIYSNqghHXf8hOicJTWGQHWGqoWCFxrBDvtaU2OTKyaQ1977DvK5lVPLxfHzHStm/1BSFbRW3XQ9zwhcS3mWWmRgWjvooMyFSe8oxTr9iOHN6tHsV5SRIHfD8hhkWoDvVSulB/0cry4HGegWwDdsMuO+jwm5N7CYwYYRGi+8gVVpwm0kegB5+opbgul4kQycmVkuwd4agWIU8zqI49cpuPgB3S2yBbrp/3tOCbuSogPh7xzk6FgoZArBY90QLaXoHJoK/CFysGH8XwyjSOAQTsEb0n62llESBWw==";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String generateToken(TokenData data) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setSubject(data.getUsername())
                .claim("id", data.getId())
                .claim("username", data.getUsername())
                .claim("email", data.getEmail())
                .claim("role", data.getRole())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }
}
