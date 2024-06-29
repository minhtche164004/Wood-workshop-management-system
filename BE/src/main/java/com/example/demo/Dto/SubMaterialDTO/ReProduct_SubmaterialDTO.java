package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReProduct_SubmaterialDTO {
    private int id;
    private String sub_name;
    private int sub_id;
    private double quantity;
    private String sub_type;
}
