package com.fooddelivery.restaurant.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to generate JWT tokens for testing purposes
 * Run this class to generate tokens for different user roles
 */
public class JwtTokenGenerator {
    
    // This must match the secret in application.yml
    private static final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds
    
    public static void main(String[] args) {
        System.out.println("=== JWT Token Generator ===\n");
        
        // Generate token for RESTAURANT_OWNER (userId: 1)
        String ownerToken = generateToken("1", "RESTAURANT_OWNER");
        System.out.println("RESTAURANT_OWNER Token (userId: 1):");
        System.out.println(ownerToken);
        System.out.println("\n" + "=".repeat(80) + "\n");
        
        // Generate token for RESTAURANT_OWNER (userId: 2)
        String owner2Token = generateToken("2", "RESTAURANT_OWNER");
        System.out.println("RESTAURANT_OWNER Token (userId: 2):");
        System.out.println(owner2Token);
        System.out.println("\n" + "=".repeat(80) + "\n");
        
        // Generate token for ADMIN
        String adminToken = generateToken("999", "ADMIN");
        System.out.println("ADMIN Token (userId: 999):");
        System.out.println(adminToken);
        System.out.println("\n" + "=".repeat(80) + "\n");
        
        System.out.println("Copy these tokens and use them in Postman!");
        System.out.println("Add to Headers: Authorization: Bearer <token>");
    }
    
    private static String generateToken(String userId, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("role", role);
        
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .claims(claims)
                .subject(userId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
}
