package com.example.demo.Dto.UserDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditUserDTO {
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


    private Integer status_id;
    @NotNull(message = "MUST_REQUIRED")
    private Integer position_id;

//    @NotNull(message = "MUST_REQUIRED")
//    private String role_name;
@NotNull(message = "MUST_REQUIRED")
    private Integer role_id;


    @NotNull(message = "MUST_REQUIRED")
    private String role_name;

    private String bank_name;
    private String bank_number;
    private String city_province;
    private String district;
    private String wards;



    public EditUserDTO( String email, String username, String phoneNumber, String address, String fullname, String bank_name, String bank_number) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullname = fullname;
        this.bank_name =bank_name;
        this.bank_number=bank_number;
    }

}

