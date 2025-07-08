package com.example.mainBase.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String SECRET_KEY;

  public SecretKey getKey() {
    byte[] keyBytes = SECRET_KEY.getBytes();
    return Keys.hmacShaKeyFor(
        keyBytes);
  }

  public String generateAccessToken(UserDetails userDetails) {

    List<String> roles = userDetails.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .toList();

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("roles", roles)
        .claim("type", "at")
        .issuedAt(new Date(System.currentTimeMillis()))
//        .expiration(new Date(System.currentTimeMillis() + 1000 * 10))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getKey())
        .compact();
  }

  public String generateRefreshToken(UserDetails userDetails) {

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim("type", "rt")
        .issuedAt(new Date(System.currentTimeMillis()))
//        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
        .signWith(getKey())
        .compact();
  }

  public String extractUserEmail(String token) {
    return extractAllClaims(token).getSubject();
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getKey())
        .build()
        .parseSignedClaims(token)           //header, body, signature xuttinxa
        .getPayload();
  }

  public boolean isTokenExpired(String token) {
    return extractAllClaims(token).getExpiration().before(new Date());
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUserEmail(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

  public Date extractExpiryDate(String token) {
    return extractAllClaims(token).getExpiration();
  }

}
