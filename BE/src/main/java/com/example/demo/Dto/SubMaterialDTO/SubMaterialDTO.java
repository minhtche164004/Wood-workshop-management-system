package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubMaterialDTO implements Serializable {
    private String sub_material_name;
    private Integer material_id;
    private String description;
    private Integer quantity;
    private BigDecimal unit_price;

}
