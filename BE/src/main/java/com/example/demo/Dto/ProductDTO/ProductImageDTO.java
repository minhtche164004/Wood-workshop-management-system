package com.example.demo.Dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDTO implements Serializable {
    private int productImageId;
    private String image_name;
    private String fullPath;
    private String fileOriginalName;
    private String extension_name;
    private int product_id;
}
