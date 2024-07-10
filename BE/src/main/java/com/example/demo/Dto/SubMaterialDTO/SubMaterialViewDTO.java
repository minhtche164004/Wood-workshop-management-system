package com.example.demo.Dto.SubMaterialDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

    @Data

    @NoArgsConstructor
    public class SubMaterialViewDTO implements Serializable {

        private int subMaterialId;
        private String subMaterialName;

        private Integer materialId;

        private String description;

        private String material_name;

        //    @NotNull(message = "MUST_REQUIRED") // Không được để trống
//    @Min(value = 0, message = "QUANTITY_INVALID") // Giá trị tối thiểu là 0
        //  @Digits(integer = 10, fraction = 0, message = "QUANTITY_INVALID") // Chỉ được nhập số nguyên
        private Double quantity;

        //  @DecimalMin(value = "0.0", inclusive = false, message = "PRICE_INVALID") // Giá trị tối thiểu lớn hơn 0
        private BigDecimal unit_price;

        public SubMaterialViewDTO(int subMaterialId,String subMaterialName, Integer materialId, String description, String material_name, Double quantity, BigDecimal unit_price) {
            this.subMaterialId=subMaterialId;
            this.subMaterialName = subMaterialName;
            this.materialId = materialId;
            this.description = description;
            this.material_name = material_name;
            this.quantity = quantity;
            this.unit_price = unit_price;
        }

        public SubMaterialViewDTO(String subMaterialName, String description, Double quantity, BigDecimal unit_price, Integer materialId) {
            this.subMaterialName = subMaterialName;
            this.description = description;
            this.quantity = quantity;
            this.unit_price = unit_price;
            this.materialId = materialId;
        }
    }


