package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class SubMateProductDTO {
    private String material_id;
    private String sub_material_id;
    private String material_type;
    private BigDecimal unitPrice;
    private double product_sub_quantity;

    public SubMateProductDTO(String material_id, String sub_material_id, String material_type, BigDecimal unitPrice, double product_sub_quantity) {
        this.material_id = material_id;
        this.sub_material_id = sub_material_id;
        this.material_type = material_type;
        this.unitPrice = unitPrice;
        this.product_sub_quantity = product_sub_quantity;
    }
}
