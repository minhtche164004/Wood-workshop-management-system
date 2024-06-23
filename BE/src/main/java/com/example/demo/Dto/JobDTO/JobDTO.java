package com.example.demo.Dto.JobDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDTO {

    @NotNull(message = "MUST_REQUIRED")
    private Integer employee_id;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    @NotNull(message = "MUST_REQUIRED")
    private Date finish;
    @NotNull(message = "MUST_REQUIRED")
    private Date start;
    @NotNull(message = "MUST_REQUIRED")
    private Integer product_quatity;
    @NotNull(message = "MUST_REQUIRED")
    private BigDecimal cost;
    @NotNull(message = "MUST_REQUIRED")
    private String job_name;
    private Integer order_detail_id;
    private Integer product_id;
    private Integer request_product_id;



}
