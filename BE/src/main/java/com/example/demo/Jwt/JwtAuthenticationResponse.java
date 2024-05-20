package com.example.demo.Jwt;

import com.example.demo.Entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@Getter
@Setter
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
    private UserDetails user;
}
