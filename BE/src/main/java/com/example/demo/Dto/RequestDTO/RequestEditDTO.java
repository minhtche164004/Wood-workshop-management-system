package com.example.demo.Dto.RequestDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestEditDTO {

//    @NotNull(message = "MUST_REQUIRED")
//    private Integer status_id;
//    @NotNull(message = "MUST_REQUIRED")
//    private Date requestDate;
//    private String response;
    private int status_id;
    private String response;
//    private List<String> files;
}
