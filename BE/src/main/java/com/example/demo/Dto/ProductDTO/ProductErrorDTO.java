package com.example.demo.Dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductErrorDTO {
//    private String code;
    private String des;
    private String solution;
    private Boolean isFixed;
    private Integer quantity;
}
