package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestProductEditDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public interface ProductService {
    Products AddNewProduct(ProductAddDTO productAddDTO, MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal);
//    Products AddNewProduct(ProductAddDTO productAddDTO);
    List<Products> GetAllProductForCustomer();
    List<Products> GetAllProductForAdmin();
    Products EditProduct(int id, ProductEditDTO productEditDTO,MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) throws Exception;
    //xuất nguyên liệu cho sản phẩm có sẵn

    List<Products> findByPriceRange(BigDecimal min, BigDecimal max);


     Products GetProductById(int product_id);

    List<Products> findProductByNameCode(String key);

    ProductDTO_Show GetProductByIdWithImage(int id);

    Products UpdateStatusProduct(int product_id, int status_id);


    void DeleteProduct(int product_id);
    void DeleteRequestProduct(int re_product_id);

    RequestProducts EditRequestProduct(int id, RequestProductEditDTO requestProductEditDTO, MultipartFile[] multipartFiles) throws IOException;






    public List<Products> filterProductForCustomer(String search, List<Integer> categoryIds, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection);


    public List<Products> filterProductsForAdmin(String search, Integer categoryId, Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection);

    List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId);

    List<SubMateProductRequestDTO> getRequestProductSubMaterialByRequestProductIdDTO(int re_productId);



}
