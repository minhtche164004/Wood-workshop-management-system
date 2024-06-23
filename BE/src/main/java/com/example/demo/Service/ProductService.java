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
//    Products AddNewProduct(ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal);
    Products AddNewProduct(ProductDTO1 productDTO1);
    List<Products> GetAllProductForCustomer();
    List<Products> GetAllProductForAdmin();
    Products EditProduct(int id,ProductDTO1 productDTO1);
    //xuất nguyên liệu cho sản phẩm có sẵn
    ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int product_id, Map<Integer, Double> subMaterialQuantities);

    //xuất nguyên liệu cho sản phẩm  theo yêu cầu

    ResponseEntity<ApiResponse<List<RequestProductsSubmaterials>>> createExportMaterialProductRequest(int request_product_id, Map<Integer, Double> subMaterialQuantities);

     Products GetProductById(int product_id);

    List<Products> findProductByNameCode(String key);

    ProductDTO_Show GetProductByIdWithImage(int id);

    Products UpdateStatusProduct(int product_id, int status_id);






}
