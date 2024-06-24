package com.example.demo.Dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestProductDTO {
    @NotNull(message = "MUST_REQUIRED")
    private String requestProductName;
//    @NotNull(message = "MUST_REQUIRED")
    private String description;
    @NotNull(message = "MUST_REQUIRED") // Không được để trống
    @DecimalMin(value = "0.0", inclusive = false, message = "PRICE_INVALID")
    private BigDecimal price;
//    @Min(value = 0, message = "QUANTITY_INVALID")
//    private Integer quantity;
    @NotNull(message = "MUST_REQUIRED")
    private Date completionTime;
    @NotNull(message = "MUST_REQUIRED")
    private int request_id;
    private List<String> files;
//    private int status_id;

//    private String image;
}


