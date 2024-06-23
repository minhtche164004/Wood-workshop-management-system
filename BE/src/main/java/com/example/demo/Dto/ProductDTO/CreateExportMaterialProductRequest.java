package com.example.demo.Dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExportMaterialProductRequest {
    private int productId;
    private Map<Integer, Double> subMaterialQuantities;
}
