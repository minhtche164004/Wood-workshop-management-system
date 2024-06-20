package com.example.demo.Dto.OderDTO;

import com.example.demo.Entity.Requestimages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestUpdateDTO {
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

}
