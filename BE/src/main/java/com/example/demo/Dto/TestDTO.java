package com.example.demo.Dto;

import lombok.Data;

@Data
public class TestDTO {
    private String username;
   private String address;
    public TestDTO(String username, String address) {
        this.username = username;
        this.address = address;
    }
}
