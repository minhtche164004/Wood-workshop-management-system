package com.example.demo.Dto.ProductDTO;

import com.example.demo.Entity.Product_Requestimages;
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
public class RequestProductAllDTO {
    private Integer id;
    private String requestProductName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Date completionTime;
    private int request_id;
    private List<Product_Requestimages> imagesList;

}
