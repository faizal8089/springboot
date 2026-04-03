package com.faizal.springboot.security;


import com.faizal.springboot.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, User user){
        String username = extractUsername(token);
        return username.equals(user.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = secretKey.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //extract payload (claims)
    private Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}

/**
 * JwtUtil — handles everything related to JWT tokens.
 *
 * WHAT IS A JWT:
 * A JWT is a signed string with 3 parts: Header.Payload.Signature
 * Header    → algorithm used (HS256)
 * Payload   → data inside token (email, issued time, expiry)
 * Signature → proof token hasn't been tampered with
 *
 * CONTROL FLOW:
 *
 * 1. USER LOGS IN
 *    AuthService.login()
 *        → calls generateToken(user)
 *        → token created with email + timestamps, signed with secret key
 *        → token string returned to client
 *
 * 2. USER MAKES A PROTECTED REQUEST
 *    JwtAuthFilter intercepts every request
 *        → reads "Authorization: Bearer <token>" from header
 *        → calls extractUsername(token) to get email from token payload
 *        → loads user from DB using that email
 *        → calls isTokenValid(token, user) to verify token is genuine
 *        → if valid → sets user in SecurityContext → request proceeds
 *        → if invalid → 401 Unauthorized
 *
 * METHOD RESPONSIBILITIES:
 *
 * generateToken(user)
 *    → called by: AuthService.login()
 *    → builds JWT with email as subject, current time, expiry time
 *    → signs it with secret key using HS256 algorithm
 *    → returns compact token string
 *
 * extractUsername(token)
 *    → called by: JwtAuthFilter
 *    → decodes token payload
 *    → returns the email (subject) stored inside
 *
 * isTokenValid(token, user)
 *    → called by: JwtAuthFilter
 *    → checks email in token matches the loaded user
 *    → checks token has not expired
 *    → returns true only if both checks pass
 *
 * isTokenExpired(token)  [private]
 *    → called by: isTokenValid()
 *    → extracts expiry timestamp from token
 *    → compares against current time
 *    → returns true if expired
 *
 * extractClaims(token)  [private]
 *    → called by: extractUsername() + isTokenExpired()
 *    → decodes and returns the full payload (Claims object)
 *    → Claims contains subject, issuedAt, expiration
 *
 * getSignKey()  [private]
 *    → called by: generateToken() + extractClaims()
 *    → converts secret string from yaml into a cryptographic SecretKey
 *    → used to both SIGN tokens and VERIFY them
 *    → same key must be used for both — if key changes, all tokens invalid
 */