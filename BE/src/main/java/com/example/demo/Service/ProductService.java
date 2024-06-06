package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Entity.Products;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Products AddNewProduct(ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal) ;





}
