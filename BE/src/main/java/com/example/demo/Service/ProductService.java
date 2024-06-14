package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO_Show;
import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;
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
    List<ProductDTO_Show> GetAllProduct();
    RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO,MultipartFile[] multipartFiles);
    Products EditProduct(int id,ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal);
    ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int product_id, Map<Integer, Integer> subMaterialQuantities);


}
