package com.example.demo.Dto.UserDTO;

import com.example.demo.Entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInforDTO {
    private Integer inforId;
    private String phoneNumber;
    private String fullname;
    private String address;
    private String email;
    private String bank_name;
    private String bank_number;
    private String city_province;
    private String district;
    private String wards;
    private Integer has_Account;

    public UserInforDTO(Integer inforId, String phoneNumber, String fullname, String address, String email,
                        String bank_name, String bank_number, String city_province, String district, String wards, Integer has_Account) {
        this.inforId = inforId;
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.address = address;
        this.email = email;
        this.bank_name = bank_name;
        this.bank_number = bank_number;
        this.city_province = city_province;
        this.district = district;
        this.wards = wards;
        this.has_Account = has_Account;
    }
}
