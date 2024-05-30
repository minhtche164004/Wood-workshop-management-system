package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO;
import com.example.demo.Entity.Products;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Products AddNewProduct(ProductDTO productDTO);
}
