package com.example.demo.Dto.MaterialDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDTO implements Serializable {
    @NotNull(message = "MUST_REQUIRED")
    private String materialName;
}
