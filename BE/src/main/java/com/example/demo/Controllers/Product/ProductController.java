package com.example.demo.Controllers.Product;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.SubMaterialService;
import com.example.demo.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/product/")
@AllArgsConstructor
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;



    @GetMapping("/GetAllProduct")
    public ApiResponse<?> getAllProduct(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(productRepository.findAll());
        return apiResponse;

    }
    @GetMapping("/GetAllCategory")
    public ApiResponse<?> getAllCategory(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(categoryRepository.findAll());
        return apiResponse;

    }
    @PostMapping("/AddNewProduct")
    public ApiResponse<?> AddNewProduct(@RequestBody @Valid ProductDTO productDTO){
        ApiResponse<Products> apiResponse= new ApiResponse<>();
        apiResponse.setResult(productService.AddNewProduct(productDTO));
        return apiResponse;
    }
}
