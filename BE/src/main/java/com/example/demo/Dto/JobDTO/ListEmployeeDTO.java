package com.example.demo.Dto.JobDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListEmployeeDTO {
    private int user_id;
    private int quantity_product;
    private BigDecimal cost;
}
