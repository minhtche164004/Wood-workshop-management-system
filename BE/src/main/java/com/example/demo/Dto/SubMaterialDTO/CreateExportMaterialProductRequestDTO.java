package com.example.demo.Dto.SubMaterialDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateExportMaterialProductRequestDTO {
    private Map<Integer, Double> subMaterialQuantities;
}
