package com.example.demo.Dto.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    @NotNull(message = "MUST_REQUIRED")
    private String username;
    @NotNull(message = "MUST_REQUIRED")
    private String email;
    @NotNull(message = "MUST_REQUIRED")
    private String phoneNumber;
    @NotNull(message = "MUST_REQUIRED")
    private String address;
    @NotNull(message = "MUST_REQUIRED")
    private String fullname;
    @NotNull(message = "MUST_REQUIRED")
    private String status_name;
    @NotNull(message = "MUST_REQUIRED")
    private String position_name;
    @NotNull(message = "MUST_REQUIRED")
    private String role_name;

    public UserDTO(String email, String username, String phoneNumber, String address, String fullname) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullname = fullname;
    }
}

