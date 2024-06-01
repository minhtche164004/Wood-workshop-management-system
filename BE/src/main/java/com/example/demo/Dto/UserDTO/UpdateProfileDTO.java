package com.example.demo.Dto.UserDTO;

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

}