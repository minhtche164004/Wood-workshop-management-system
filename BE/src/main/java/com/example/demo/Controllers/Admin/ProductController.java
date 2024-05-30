package com.example.demo.Controllers.Admin;

import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin/")
@AllArgsConstructor
public class ProductController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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
}
