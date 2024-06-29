package com.example.demo.Dto.JobDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee_MaterialDTO {
    private int emp_id;
    private int product_sub_material_id;
    private int request_product_submaterial_id;
}
