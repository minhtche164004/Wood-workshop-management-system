package com.example.demo.Dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {
    private String product_name;
    private String description;
    private int quantity;
    private BigDecimal price;
    private int status_id;
    private String image;
    private int category_id;
    private int type;

}
