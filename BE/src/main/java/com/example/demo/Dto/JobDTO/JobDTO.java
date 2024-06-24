package com.example.demo.Dto.JobDTO;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.User;
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

    private User employee;
    private String job_name;
    private Integer quantity_product;
   // private Categories categories;
    private String description;
    @NotNull(message = "MUST_REQUIRED")
    private Date finish;
    @NotNull(message = "MUST_REQUIRED")
    private Date start;
    @NotNull(message = "MUST_REQUIRED")
    private BigDecimal cost;

}
