package com.example.course_mapping_be.security;

import com.example.course_mapping_be.constraints.Error;
import com.example.course_mapping_be.models.User;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JsonWebTokenProvider {
    private static final String JWT_SECRET = "COURSE_MAPPING";
    private static final Long JWT_EXPIRATION = 604800000L;

    public String generateToken(CustomUserDetails customUserDetails) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(customUserDetails.getUser().getId().toString())
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT");
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT");
        } catch (IllegalArgumentException e) {
            System.out.println("JWT clams string is empty");
        }
        return false;
    }

    public Long getUserIdFromRequest(HttpServletRequest request) throws Exception {
        return Long.parseLong(getUserIdFromJWT(
                getJWTFromRequest(request)
        ));
    }

    private String getJWTFromRequest(HttpServletRequest request) throws Exception {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new Exception(Error.INVALID_AUTHORIZATION.getMessage());
        }

        return token.substring(7);
    }
}
