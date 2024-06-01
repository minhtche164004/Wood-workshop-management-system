package com.example.demo.Dto.SupplierDTO;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierMaterialDTO implements Serializable {
    @NotNull(message = "MUST_REQUIRED")
    private String supplierName;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @DecimalMin(value = "0.0", inclusive = false, message = "PHONE_INVALID") // Giá trị tối thiểu lớn hơn 0
    private String phoneNumber;
    private Integer sub_material_id;
}




