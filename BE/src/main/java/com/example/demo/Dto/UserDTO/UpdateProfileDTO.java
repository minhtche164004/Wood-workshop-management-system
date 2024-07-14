package com.example.demo.Dto.UserDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO implements Serializable {

    private String username;
    private String email;
    private String phoneNumber;
    private String address;
    private String fullname;

    private String bank_name;
    private String bank_number;
    private String city;
    private String district;
    private String wards;

    private int role_id;
    private String role_name;

}