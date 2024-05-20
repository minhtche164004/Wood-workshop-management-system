package com.example.demo.Request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RefreshTokenRequest {
    private String token;
}
