package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CusInfo {
    private String fullname;
    private String address;
    private String city_province;
    private String district;
    private String wards;
    private String phone;
}
