package com.feelreal.api.service.authentication;

import com.feelreal.api.dto.authentication.TokenData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaim(token, t -> t.get("username", String.class));
    }

    public String extractId(String token) {
        return extractClaim(token, t -> t.get("id", String.class));

    }

    public String extractRole(String token) {
        return extractClaim(token, t -> t.get("role", String.class));
    }

    public LocalDate extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public String generateToken(TokenData data) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR, 1);

        return Jwts.builder()
                .setSubject(data.getUsername())
                .claim("id", data.getId())
                .claim("username", data.getUsername())
                .claim("email", data.getEmail())
                .claim("role", data.getRole())
                .setIssuedAt(now)
                .setExpiration(calendar.getTime())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isValid(String token, UserDetails userDetails) {
        TokenData tokenData = extractTokenData(token);

        if (tokenData == null) {
            return false;
        }

        if (tokenData.getId() == null || tokenData.getUsername() == null ||
                tokenData.getEmail() == null || tokenData.getRole() == null) {
            return false;
        }

        if (!tokenData.getUsername().equals(userDetails.getUsername()) ||
                userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals(tokenData.getRole()))) {
            return false;
        }

        return !extractExpiration(token).isBefore(LocalDate.now());
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

    private TokenData extractTokenData(String token) {
        Claims claims = extractAllClaims(token);

        if (claims == null) {
            return null;
        }

        return new TokenData(
                claims.get("id", String.class),
                claims.get("username", String.class),
                claims.get("email", String.class),
                claims.get("role", String.class)
        );
    }

}
