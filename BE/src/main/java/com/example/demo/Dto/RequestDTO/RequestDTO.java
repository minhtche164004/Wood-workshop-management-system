package com.example.demo.Dto.RequestDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {
//    @NotNull(message = "MUST_REQUIRED")
//    private Integer user_id;
    @NotNull(message = "MUST_REQUIRED")
    private Integer status_id;
//    @NotNull(message = "MUST_REQUIRED")
//    private Date requestDate;

    private String response;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    private String phoneNumber;
    private String fullname;
    private String address;
    private String city_province;
    private String district_province;
    private String wards_province;
    private List<String> files;
    private String email;
}
