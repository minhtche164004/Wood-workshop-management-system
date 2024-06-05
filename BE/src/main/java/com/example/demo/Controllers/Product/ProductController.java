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
import com.example.demo.Service.CategorySevice;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.SubMaterialService;
import com.example.demo.Service.UserService;
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
    @PostMapping("/AddNewProduct")
    public ApiResponse<?> AddNewProduct(@RequestBody @Valid ProductDTO productDTO){
        ApiResponse<Products> apiResponse= new ApiResponse<>();
        apiResponse.setResult(productService.AddNewProduct(productDTO));
        return apiResponse;
    }




//    @PostMapping("/image")
//    public ResponseEntity<?> uploadImage(@RequestParam(value = "productId", required = false) int productId,@RequestParam("image") List<MultipartFile> file ) throws IOException {
//        List<String> uploadImage = productService.uploadImagesList(file,productId);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(uploadImage);
//    }
////    //đọc theo từng ảnh
////    @GetMapping("/{fileName}")
////    public ResponseEntity<?> downloadImage(@PathVariable String fileName){
////        byte[] imageData=productService.dowloadImage(fileName);
////        return ResponseEntity.status(HttpStatus.OK)
////                .contentType(MediaType.valueOf("image/png"))
////                .body(imageData);
////    }
////đọc theo từng ảnh để trả về hình ảnh của từng ảnh
//@GetMapping("/{productId}/images/{imageIndex}")
//public ResponseEntity<byte[]> downloadImage(
//        @PathVariable int productId,
//        @PathVariable int imageIndex
//) {
//    try {
//        List<byte[]> imageDataList = productService.downloadImagesByProductList(productId);
//
//        if (imageDataList == null || imageDataList.isEmpty() || imageIndex < 0 || imageIndex >= imageDataList.size()) {
//            throw new AppException(ErrorCode.NOT_FOUND); // Không tìm thấy ảnh
//        }
//
//        byte[] imageData = imageDataList.get(imageIndex); // Lấy ảnh theo chỉ số
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_PNG) // hoặc MediaType.IMAGE_JPEG nếu ảnh của bạn là JPEG
//                .body(imageData);
//    } catch (AppException e) {
//        throw new AppException(ErrorCode.IMAGE_INVALID);
//    }
//}
}
