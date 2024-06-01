package com.example.demo.Dto.UserDTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {
    @Size(min=4,message = "USERNAME_INVALID")
    private String username;
    private String address;
    private String status;
    private String position;
    private String role;
    private String email;

}
