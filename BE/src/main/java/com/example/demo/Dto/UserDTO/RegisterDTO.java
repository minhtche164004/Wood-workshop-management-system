package com.example.demo.Dto.UserDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    @Size(min=5,message = "USERNAME_INVALID")
    private String username;
    @Size(min=4,message = "PASS_INVALID")
    private String password;
    @NotNull(message = "MUST_REQUIRED")
    private String checkPass;
    @NotNull(message = "MUST_REQUIRED")
    private String email;
    @NotNull(message = "MUST_REQUIRED")
    @DecimalMin(value = "0.0", inclusive = false, message = "PHONE_INVALID")
    private String phoneNumber;
    @NotNull(message = "MUST_REQUIRED")
    private String address;
    @NotNull(message = "MUST_REQUIRED")
    private String fullname;
    @NotNull(message = "MUST_REQUIRED")
    private int status;
//    @NotNull(message = "MUST_REQUIRED")
//    private int position;
    @NotNull(message = "MUST_REQUIRED")
    private String bank_name;

    @NotNull(message = "MUST_REQUIRED")
    private String bank_number;

    @NotNull(message = "MUST_REQUIRED")
    private String city;

    @NotNull(message = "MUST_REQUIRED")
    private String district;

    @NotNull(message = "MUST_REQUIRED")
    private String wards;


}
