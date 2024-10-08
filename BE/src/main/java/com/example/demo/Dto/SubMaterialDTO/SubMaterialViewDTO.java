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

        private String materialName;

        //    @NotNull(message = "MUST_REQUIRED") // Không được để trống
//    @Min(value = 0, message = "QUANTITY_INVALID") // Giá trị tối thiểu là 0
        //  @Digits(integer = 10, fraction = 0, message = "QUANTITY_INVALID") // Chỉ được nhập số nguyên
        private Double quantity;

        //  @DecimalMin(value = "0.0", inclusive = false, message = "PRICE_INVALID") // Giá trị tối thiểu lớn hơn 0
        private BigDecimal unitPrice;
        private BigDecimal inputPrice;
        private String type;
        private String code;
        private int input_id;
        private String code_input;
        private String reason_export;

        public SubMaterialViewDTO(int subMaterialId,String subMaterialName, Integer materialId, String description,
                                  String materialName, Double quantity, BigDecimal unitPrice,BigDecimal inputPrice,String type,String code,int input_id,String code_input,String reason_export) {
            this.subMaterialId=subMaterialId;
            this.subMaterialName = subMaterialName;
            this.materialId = materialId;
            this.description = description;
            this.materialName = materialName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.inputPrice = inputPrice;
            this.type=type;
            this.code=code;
            this.input_id=input_id;
            this.code_input=code_input;
            this.reason_export=reason_export;
        }

        public SubMaterialViewDTO(String subMaterialName, String description, Double quantity, BigDecimal unit_price, Integer materialId,String reason_export) {
            this.subMaterialName = subMaterialName;
            this.description = description;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.materialId = materialId;
            this.reason_export=reason_export;
        }
    }


