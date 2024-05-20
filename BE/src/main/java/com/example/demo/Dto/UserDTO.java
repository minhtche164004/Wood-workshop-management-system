package com.example.demo.Dto;

import jakarta.validation.constraints.Size;
import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {

    @Size(min=5,message = "USERNAME_INVALID")
    private String username;
    @Size(min=4,message = "PASS_INVALID")
    private String password;
    private String checkPass;
    private String email;
    private String phoneNumber;
    private String address;
    private String fullname;
    private Boolean status;
    private String position;
    private String role;

}


