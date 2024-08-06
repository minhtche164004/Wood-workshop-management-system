package com.example.demo.Controller;
import com.example.demo.Controllers.Product.ProductController;
import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Entity.Products;
import com.example.demo.Repository.*;
import com.example.demo.Service.*;
import com.example.demo.Response.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private CloudinaryService cloudinaryService;

    @MockBean
    private Status_Product_Repository statusProductRepository;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testGetAllProductForCustomer() throws Exception {
        Products product = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productService.GetAllProductForCustomer()).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/auth/product/getAllProductForCustomer"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewProduct() throws Exception {
        // Prepare your DTO with valid values
        ProductAddDTO productAddDTO = new ProductAddDTO();
        productAddDTO.setProduct_name("Product1");
        productAddDTO.setDescription("Description1");
        productAddDTO.setPrice(BigDecimal.valueOf(100));
        productAddDTO.setCategory_id(1);
        productAddDTO.setType(1);

        // Convert DTO to JSON
        String productAddDTOJson = new Gson().toJson(productAddDTO);

        // Mock files
        MockMultipartFile fileThumbnail = new MockMultipartFile("file_thumbnail", "image1.jpg", "image/jpeg", "image content".getBytes());
        MockMultipartFile files = new MockMultipartFile("files", "file1.jpg", "image/jpeg", "file content".getBytes());

        // Mock service call
        Products savedProduct = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productService.AddNewProduct(any(ProductAddDTO.class), any(), any())).thenReturn(savedProduct);


    }
    @Test
    void testEditProduct() throws Exception {
        ProductEditDTO productEditDTO = new ProductEditDTO();
        Products updatedProduct = new Products("UpdatedProduct", "UpdatedDescription", 15, BigDecimal.valueOf(150), "updatedImage.jpg", null, null, "updatedCode", 1, null, null);
        when(productService.EditProduct(anyInt(), any(ProductEditDTO.class), any(), any())).thenReturn(updatedProduct);

        mockMvc.perform(multipart("/api/auth/product/EditProduct")
                        .param("product_id", "1"))

                .andExpect(status().isOk());
            //    .andExpect(jsonPath("$.result.productName").value("UpdatedProduct"));
    }

    @Test
    void testDeleteProduct() throws Exception {
       // when(productService.DeleteProduct(anyInt())).thenReturn("Xoá thành công");

        mockMvc.perform(delete("/api/auth/product/deleteProduct")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Xoá thành công"));
    }

    @Test
    void testGetProductById() throws Exception {
        Products product = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productService.GetProductById(anyInt())).thenReturn(product);

        mockMvc.perform(get("/api/auth/product/GetProductById")
                        .param("product_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.productName").value("Product1"));
    }

    @Test
    void testGetProductByCategory() throws Exception {
        Products product = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productRepository.findByCategory(anyInt())).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/auth/product/GetProductByCategory")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].productName").value("Product1"));
    }

    @Test
    void testGetProductByStatus() throws Exception {
        Products product = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productRepository.findByStatus(anyInt())).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/auth/product/GetProductByStatus")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].productName").value("Product1"));
    }

    @Test
    void testFindByPriceRange() throws Exception {
        Products product = new Products("Product1", "Description1", 10, BigDecimal.valueOf(100), "image1.jpg", null, null, "code1", 1, null, null);
        when(productService.findByPriceRange(any(BigDecimal.class), any(BigDecimal.class))).thenReturn(Collections.singletonList(product));

        mockMvc.perform(get("/api/auth/product/findByPriceRange")
                        .param("min", "50")
                        .param("max", "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result[0].productName").value("Product1"));
    }

    @Test
    void testGetStatusProduct() throws Exception {
        when(statusProductRepository.GetListStatusType()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/product/GetStatusProduct"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isArray());
    }

    @Test
    void testDeleteImage() throws Exception {
        when(cloudinaryService.deleteImage(anyString())).thenReturn(Collections.singletonMap("status", "success"));

        mockMvc.perform(delete("/api/auth/product/deleteimages")
                        .param("id", "imageId"))
                .andExpect(status().isOk())
               ;
    }
}
