package com.example.demo.Controllers.Product;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.Productimages;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth/product/")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategorySevice categorySevice;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private UploadImageService uploadImageService;

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
    @GetMapping("/getAllCategoryName")
    public ApiResponse<?> getAllCategoryName(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categorySevice.GetListName());
        return apiResponse;
    }

    @PostMapping(value = "/AddNewProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewProduct(
            @RequestPart("productDTO") ProductDTO productDTO,
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
    ) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.AddNewProduct(productDTO, files,file_thumbnail));
        return apiResponse;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImage(@RequestParam("files")MultipartFile[] files,@RequestParam("product_id") int product_id){
        try{
            return ResponseEntity.ok().body(uploadImageService.uploadFile(files,product_id));
        }catch(Exception e){
            return  ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
}

}
