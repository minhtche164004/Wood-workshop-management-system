package com.example.demo.Controllers;

import com.example.demo.Dto.ProductDTO.ProductAddDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CloudinaryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/cloudinary")
@AllArgsConstructor
public class CloudinaryController {
    @Autowired
    CloudinaryService cloudinaryService;
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewProduct(
            @RequestPart("test") MultipartFile file_thumbnail
    ) throws IOException {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(productService.AddNewProduct(productAddDTO, files, file_thumbnail));
        Map result = cloudinaryService.upload(file_thumbnail, "product_images");

        System.out.println(result.get("url"));
        return apiResponse;
    }
}
