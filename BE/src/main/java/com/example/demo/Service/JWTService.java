package com.example.demo.Service;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;

public interface JWTService {
    String extractUserName(String token);
    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
    boolean isTokenValid(String token,UserDetails userDetails);
   // String generateRefreshToken(Map<String,Object> extraClaims, UserDetails userDetails);
}