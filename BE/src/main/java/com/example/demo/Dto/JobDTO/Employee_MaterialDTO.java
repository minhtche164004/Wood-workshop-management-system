package com.example.demo.Dto.JobDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee_MaterialDTO {
    private int emp_id;
    private String emp_name;
    private String  position_name;
    private String  sub_material_id;
    private String sub_material_name;
    private double quantity;

}
