package com.example.demo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.demo.Dto.ProductDTO.ProductAddDTO;
import com.example.demo.Dto.ProductDTO.ProductDetailDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.CloudinaryService;
import com.example.demo.Service.Impl.ProductServiceImpl;
import com.example.demo.Service.UploadImageService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private SubMaterialsRepository subMaterialsRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CheckConditionService checkConditionService;
    @Mock
    private UploadImageService uploadImageService;
    @Mock
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Mock
    private ProductImageRepository productImageRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private Status_Product_Repository statusProductRepository;
    @Mock
    CloudinaryService cloudinaryService;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private ProcessproducterrorRepository processproducterrorRepository;
    @Mock
    private WishListRepository wishListRepository;
    @Mock
    private RequestProductRepository requestProductRepository;
    @Mock
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
//    @Mock
//    private Product_RequestimagesRepository productRequestimagesRepository;
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private ProductImageRepository productImageRepository;
//
//    @Mock
//    private ProductSubMaterialsRepository productSubMaterialsRepository;

    @InjectMocks
    private ProductServiceImpl productService;


    private Products mockProduct1;
    private Products mockProduct2;

    @BeforeEach
    public void setUp() {
        mockProduct1 = new Products();
        mockProduct1.setProductId(1);
        mockProduct1.setProductName("Product 1");
        mockProduct1.setPrice(new BigDecimal("100"));
        mockProduct1.setImage("image1");

        mockProduct2 = new Products();
        mockProduct2.setProductId(2);
        mockProduct2.setProductName("Product 2");
        mockProduct2.setPrice(new BigDecimal("200"));
        mockProduct2.setImage("image2");
    }

    @Test
    public void testFilterProductsForAdmin_WithCriteria() {
        String search = "test";
        Integer categoryId = 1;
        Integer statusId = 1;
        BigDecimal minPrice = new BigDecimal("50");
        BigDecimal maxPrice = new BigDecimal("150");
        String sortDirection = "asc";

        List<Products> filteredProducts = new ArrayList<>();
        filteredProducts.add(mockProduct1);

        when(productRepository.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice))
                .thenReturn(filteredProducts);

        List<Products> result = productService.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice, sortDirection);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
        assertEquals("image1", result.get(0).getImage());
    }
    @Test
    void testAddNewProductWithInvalidPrice() {
        // Given
        ProductAddDTO productAddDTO = new ProductAddDTO();
        productAddDTO.setProduct_name("New Product");
        productAddDTO.setDescription("Description");
        productAddDTO.setPrice(new BigDecimal("0.00")); // Invalid price
        productAddDTO.setCategory_id(1);

        when(checkConditionService.checkInputPrice(productAddDTO.getPrice())).thenReturn(false);

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> {
            productService.AddNewProduct(productAddDTO, new MultipartFile[0], null);
        });

        assertEquals(ErrorCode.PRICE_INVALID, exception.getErrorCode());
    }

    @Test
    public void testFilterProductsForAdmin_NoCriteria_ReturnAll() {
        String search = null;
        Integer categoryId = null;
        Integer statusId = null;
        BigDecimal minPrice = null;
        BigDecimal maxPrice = null;
        String sortDirection = "asc";

        List<Products> allProducts = new ArrayList<>();
        allProducts.add(mockProduct1);
        allProducts.add(mockProduct2);

        when(productRepository.findAll()).thenReturn(allProducts);

        List<Products> result = productService.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice, sortDirection);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getProductName());
        assertEquals("Product 2", result.get(1).getProductName());
    }

    @Test
    public void testFilterProductsForAdmin_NoProductsFound() {
        String search = "test";
        Integer categoryId = 1;
        Integer statusId = 1;
        BigDecimal minPrice = new BigDecimal("50");
        BigDecimal maxPrice = new BigDecimal("150");
        String sortDirection = "asc";

        when(productRepository.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice))
                .thenReturn(Collections.emptyList());

        AppException exception = assertThrows(AppException.class, () -> {
            productService.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice, sortDirection);
        });

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }
    @Test
    void testGetProductByIdWithImage_Success() {
        // Given
        int productId = 1;
        Products product = new Products();
        product.setProductId(productId);
        product.setProductName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setImage("/assets/product_image.jpg");
        product.setCompletionTime((java.sql.Date.valueOf("2026-01-01")));
        product.setEnddateWarranty((java.sql.Date.valueOf("2026-01-01")));
        product.setCode("PD001");
        product.setType(1);
        product.setCategories(null);

        Productimages productImage = new Productimages();
        productImage.setFullPath("/assets/product_image_1.jpg");

        List<Productimages> images = Collections.singletonList(productImage);

        SubMaterialViewDTO subMaterialViewDTO = new SubMaterialViewDTO();
        List<SubMaterialViewDTO> subMaterials = Collections.singletonList(subMaterialViewDTO);

        when(productRepository.findById(productId)).thenReturn(product);
        when(productImageRepository.findImageByProductId(productId)).thenReturn(images);
        when(productSubMaterialsRepository.GetSubMaterialByProductId(productId)).thenReturn(subMaterials);

        // When
        ProductDetailDTO result = productService.GetProductByIdWithImage(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Product Name", result.getProductName());
        assertEquals("Product Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(100), result.getPrice());
        assertEquals("assets/product_image.jpg", result.getImage());
        assertEquals(java.sql.Date.valueOf("2026-01-01"), result.getCompletionTime());
        assertEquals(java.sql.Date.valueOf("2026-01-01"), result.getEnddateWarranty());
        assertEquals("PD001", result.getCode());
        assertEquals(1, result.getType());

        assertNull(result.getCategories());
        assertEquals(1, result.getImageList().size());
        assertEquals("assets/product_image_1.jpg", result.getImageList().get(0).getFullPath());
        assertEquals(1, result.getSub_material_name().size());
    }

    @Test
    void testGetProductByIdWithImage_NoImages() {
        // Given
        int productId = 2;
        Products product = new Products();
        product.setProductId(productId);
        product.setProductName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.valueOf(200));
        product.setImage("/assets/product_image.jpg");
        product.setCompletionTime((java.sql.Date.valueOf("2026-01-01")));
        product.setEnddateWarranty((java.sql.Date.valueOf("2026-01-01")));
        product.setCode("PD002");
        product.setType(1);
        product.setCategories(null);

        List<Productimages> images = Collections.emptyList();

        SubMaterialViewDTO subMaterialViewDTO = new SubMaterialViewDTO();
        List<SubMaterialViewDTO> subMaterials = Collections.singletonList(subMaterialViewDTO);

        when(productRepository.findById(productId)).thenReturn(product);
        when(productImageRepository.findImageByProductId(productId)).thenReturn(images);
        when(productSubMaterialsRepository.GetSubMaterialByProductId(productId)).thenReturn(subMaterials);

        // When
        ProductDetailDTO result = productService.GetProductByIdWithImage(productId);

        // Then
        assertNotNull(result);
        assertEquals(productId, result.getProductId());
        assertEquals("Product Name", result.getProductName());
        assertEquals("Product Description", result.getDescription());
        assertEquals(BigDecimal.valueOf(200), result.getPrice());
        assertEquals("assets/product_image.jpg", result.getImage());
        assertEquals((java.sql.Date.valueOf("2026-01-01")), result.getCompletionTime());
        assertEquals((java.sql.Date.valueOf("2026-01-01")), result.getEnddateWarranty());
        assertEquals("PD002", result.getCode());
        assertEquals(1, result.getType());

        assertNull(result.getCategories());
        assertTrue(result.getImageList().isEmpty());
        assertEquals(1, result.getSub_material_name().size());
    }

    @Test
    void testGetProductByIdWithImage_ProductNotFound() {
        // Given
        int productId = 100;

        when(productRepository.findById(productId)).thenReturn(null);

        // When & Then
        AppException exception = assertThrows(AppException.class, () -> productService.GetProductByIdWithImage(productId));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testAddNewProduct_ProductNameExists() throws IOException {
        // Given
        ProductAddDTO productAddDTO = new ProductAddDTO();
        productAddDTO.setProduct_name("Product 1");
        productAddDTO.setDescription("Description");
        productAddDTO.setPrice(BigDecimal.valueOf(-1));
        productAddDTO.setCategory_id(3);

        MultipartFile[] multipartFiles = new MultipartFile[] { mock(MultipartFile.class) };
        MultipartFile multipartFileThumbnail = mock(MultipartFile.class);
        Product_Thumbnail thumbnail = new Product_Thumbnail("/thumbnail.jpg");


        // When & Then
        AppException thrown = assertThrows(AppException.class, () -> {
            productService.AddNewProduct(productAddDTO, multipartFiles, multipartFileThumbnail);
        });

        assertEquals(ErrorCode.PRICE_INVALID, thrown.getErrorCode());
    }


}
