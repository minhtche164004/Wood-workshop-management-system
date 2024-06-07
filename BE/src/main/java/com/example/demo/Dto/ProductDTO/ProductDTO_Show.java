package com.example.demo.Dto.ProductDTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO_Show {
    @NotNull(message = "MUST_REQUIRED")
    private String product_name;
    @NotNull(message = "MUST_REQUIRED")
    private String description;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @Min(value = 0, message = "QUANTITY_INVALID") // Giá trị tối thiểu là 0
    @Digits(integer = 10, fraction = 0, message = "QUANTITY_INVALID") // Chỉ được nhập số nguyên
    private int quantity;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @DecimalMin(value = "0.0", inclusive = false, message = "PRICE_INVALID") // Giá trị tối thiểu lớn hơn 0
    private BigDecimal price;
    @NotNull(message = "MUST_REQUIRED")
    private int status_id;
       @NotNull(message = "MUST_REQUIRED")
   private String images;
    @NotNull(message = "MUST_REQUIRED")
    private int category_id;
    @NotNull(message = "MUST_REQUIRED")
    private int type;
}
