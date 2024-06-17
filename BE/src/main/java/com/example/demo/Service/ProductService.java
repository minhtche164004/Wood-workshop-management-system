package com.example.demo.Service;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO_Show;
import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
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
    RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO,MultipartFile[] multipartFiles);
    Products EditProduct(int id,ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal);
    ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int product_id, Map<Integer, Integer> subMaterialQuantities);
    Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles);
     Products GetProductById(int product_id);
     List<RequestProducts> GetAllProductRequest();
    List<Requests> GetAllRequests();

    List<Products> findProductByNameCode(String key);



}
