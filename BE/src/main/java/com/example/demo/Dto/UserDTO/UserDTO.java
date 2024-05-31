package com.example.demo.Dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String fullname;
    private String status_name;
    private String position_name;
    private String role_name;

    public UserDTO(String email, String username, String phoneNumber, String address, String fullname) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullname = fullname;
    }
}

