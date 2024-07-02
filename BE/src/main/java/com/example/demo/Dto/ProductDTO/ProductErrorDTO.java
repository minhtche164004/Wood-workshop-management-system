package com.example.demo.Dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductErrorDTO {
//    private String code;
    private String description;
    private String solution;
}
