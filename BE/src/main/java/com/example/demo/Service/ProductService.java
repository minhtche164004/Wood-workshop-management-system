package com.example.demo.Service;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Products AddNewProduct(ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal) ;
    List<Products> GetAllProduct();
    Products EditProduct(int id,ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal);
    ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int product_id, Map<Integer, Integer> subMaterialQuantities);
     Products GetProductById(int product_id);

    List<Products> findProductByNameCode(String key);

    ProductDTO_Show GetProductByIdWithImage(int id);

    Products UpdateStatusProduct(int product_id, int status_id);



}
