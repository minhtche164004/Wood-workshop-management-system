package com.example.demo.Dto.SubMaterialDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubMaterialDTO implements Serializable {
    @NotNull(message = "MUST_REQUIRED")
    private String sub_material_name;
    @NotNull(message = "MUST_REQUIRED")
    private Integer material_id;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @Min(value = 0, message = "QUANTITY_INVALID") // Giá trị tối thiểu là 0
  //  @Digits(integer = 10, fraction = 0, message = "QUANTITY_INVALID") // Chỉ được nhập số nguyên
    private Double quantity;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
  //  @DecimalMin(value = "0.0", inclusive = false, message = "PRICE_INVALID") // Giá trị tối thiểu lớn hơn 0
    private BigDecimal unit_price;

}
