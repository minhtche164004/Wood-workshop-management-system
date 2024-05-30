package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

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