package com.example.demo.Dto.OrderDTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EditOrderDTO {

    private Date orderDate;
    private String phoneNumber;
    private String fullname;
    private String address;
    private String city_province;
    private String district;
    private String wards;


    private Date orderFinish;
}
