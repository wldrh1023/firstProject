package com.connecthub.socialmedia.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

  private final SecretKey key;
  private final int jwtExpirationInMs;

  public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
      @Value("${jwt.expiration}") int jwtExpirationInMs) {
    this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    this.jwtExpirationInMs = jwtExpirationInMs;
  }

  public String generateToken(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(userPrincipal.getId()))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public String generateTokenFromUserId(Long userId) {
    Date expiryDate = new Date(System.currentTimeMillis() + jwtExpirationInMs);

    return Jwts.builder()
        .setSubject(Long.toString(userId))
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(key)
        .compact();
  }

  public Long getUserIdFromJWT(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return Long.parseLong(claims.getSubject());
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException ex) {
      System.err.println("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      System.err.println("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      System.err.println("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      System.err.println("JWT claims string is empty");
    }
    return false;
  }
}
