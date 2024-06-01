package com.example.demo.Dto.Category;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryNameDTO implements Serializable {
    @NotNull(message = "MUST_REQUIRED")
    private String categoryName;
}
