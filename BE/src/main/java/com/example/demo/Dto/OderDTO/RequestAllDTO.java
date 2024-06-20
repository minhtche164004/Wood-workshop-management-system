package com.example.demo.Dto.ProductDTO;

import com.example.demo.Entity.Product_Requestimages;
import com.example.demo.Entity.Requestimages;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestAllDTO {
    private Integer user_id;
    private Integer status_id;

    private Date requestDate;

    private String response;

    private String description;
    private String phoneNumber;
    private String fullname;
    private String address;
    private String city_province;
    private String district;
    private String wards;
    private List<Requestimages> imagesList;
    private String code;
}
