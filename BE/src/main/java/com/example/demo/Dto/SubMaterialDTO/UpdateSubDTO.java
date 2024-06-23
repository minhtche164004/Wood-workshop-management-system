package com.example.demo.Dto.SubMaterialDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubDTO {
    @NotNull(message = "MUST_REQUIRED")
    private String sub_material_name;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @Min(value = 0, message = "QUANTITY_INVALID") // Giá trị tối thiểu là 0
    private Double quantity;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống

    private BigDecimal unit_price;
}
