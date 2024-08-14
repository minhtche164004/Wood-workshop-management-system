package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@Data
public class SubMateProductRequestDTO {
    private int materialId;
    private int subMaterialId;
    private String subMaterialName;
    private String materialType;
    private BigDecimal unitPrice;
    private double quantity;
    private String code;
    private int input_id;

    public SubMateProductRequestDTO(int materialId, int subMaterialId, String subMaterialName, String materialType, BigDecimal unitPrice, double quantity,String code,int input_id) {
        this.materialId = materialId;
        this.subMaterialId = subMaterialId;
        this.subMaterialName = subMaterialName;
        this.materialType = materialType;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.code=code;
        this.input_id=input_id;
    }
}
