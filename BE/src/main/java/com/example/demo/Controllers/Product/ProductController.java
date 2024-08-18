package com.example.demo.Controllers.Product;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
import com.example.demo.Dto.RequestDTO.RequestProductEditDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/product/")
@CrossOrigin(origins = "http://localhost:5173")
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
    private CloudinaryService cloudinaryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private Status_Product_Repository statusProductRepository;
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();

    //    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/getAllProductForCustomer")
    public ApiResponse<?> getAllProductForCustomer() {
        ApiResponse<List> apiResponse = new ApiResponse<>();

        String cacheKey = "all_products_customer";
        List<Products> products;
        String cachedData = jedis.get(cacheKey);

        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Products>>() {
            }.getType();
            products = gson.fromJson(cachedData, type);
        } else {
            products = productService.GetAllProductForCustomer();
            String jsonData = gson.toJson(products);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
        return apiResponse;
    }
    @GetMapping("/getAllProductForAdmin")
    public ApiResponse<?> getAllProductForAdmin() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_admin";
////        jedis.del("all_products_admin");
//        List<Products> products;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Products>>() {
//            }.getType();
//
//            products = gson.fromJson(cachedData, type);
//        } else {
//            products = productService.GetAllProductForAdmin();
//            String jsonData = gson.toJson(products);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
      apiResponse.setResult(productService.GetAllProductForAdmin());


        return apiResponse;
    }


    @GetMapping("/GetProductByStatus")
    public ApiResponse<?> GetProductByStatus(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_by_status";
//        List<Products> products;
//        String cachedData = jedis.hget(cacheKey, id + "");
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Products>>() {
//            }.getType();
//
//            products = gson.fromJson(cachedData, type);
//        } else {
//            products = productRepository.findByStatus(id);
//            String jsonData = gson.toJson(products);
//            jedis.hset(cacheKey, id + "", jsonData);
//            jedis.expire(cacheKey, 1800);
//        }
        apiResponse.setResult(productRepository.findByStatus(id));
//        apiResponse.setResult(productRepository.findByStatus(id));
        return apiResponse;
    }

    @GetMapping("/GetProductByCategory")
    public ApiResponse<?> GetProductByCategory(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_by_cate";
//        List<Products> products;
//        String cachedData = jedis.hget(cacheKey, id + "");
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Products>>() {
//            }.getType();
//
//            products = gson.fromJson(cachedData, type);
//        } else {
//            products = productRepository.findByCategory(id);
//            String jsonData = gson.toJson(products);
//            jedis.hset(cacheKey, id + "", jsonData);
//            jedis.expire(cacheKey, 1800);
//        }
        apiResponse.setResult(productRepository.findByCategory(id));
//        apiResponse.setResult(productRepository.findByCategory(id));
        return apiResponse;
    }

    @PutMapping("/UpdateStatusProduct")
    public ApiResponse<?> UpdateStatusProduct(@RequestParam("product_id") int product_id, @RequestParam("status_id") int status_id) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.UpdateStatusProduct(product_id, status_id));
//        jedis.del("all_products");
//        jedis.del("all_products_by_status");
//        jedis.del("all_products_by_cate");
        return apiResponse;
    }

    @GetMapping("/ViewDetailProductById")
    public ApiResponse<?> GetProductByIdWithImage(@RequestParam("id") int id) {
        ApiResponse<ProductDetailDTO> apiResponse = new ApiResponse<>();
        jedis.del("all_sub_mate_product");
        apiResponse.setResult(productService.GetProductByIdWithImage(id));
        return apiResponse;
    }
    @GetMapping("/getRequestProductByRequestId")
    public ApiResponse<?> getRequestProductByRequestId(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(requestProductRepository.findByRequestId(id));
        return apiResponse;
    }



    @GetMapping("/findByPriceRange")
    public ApiResponse<?> GetProductByIdWithImage(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.findByPriceRange(min,max));
        return apiResponse;
    }

    @GetMapping("/findByPriceRangeRequestProduct")
    public ApiResponse<?> GetRequestProductByIdWithImage(@RequestParam("min") BigDecimal min,@RequestParam("max") BigDecimal max) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.findByPriceRange(min,max));
        return apiResponse;
    }

//    @GetMapping("/getRequestEditCusDTOById")
//    public ApiResponse<?> getRequestEditCusDTOById(@RequestParam("id") int id) {
//        ApiResponse<RequestEditCusDTO> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.getRequestEditCusDTOById(id));
//        return apiResponse;
//    }



//    @GetMapping("/GetRequestById")
//    public ApiResponse<?> GetRequestById(@RequestParam("id") int id) {
//        ApiResponse<Requests> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(productService.getRequestById(id));
//        return apiResponse;
//    }


//    @GetMapping("/GetRequestProductById")
//    public ApiResponse<?> GetRequestProductById(@RequestParam("id") int id) {
//        ApiResponse<RequestProducts> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(productService.getRequestProductsById(id));
//        return apiResponse;
//    }

    @GetMapping("/GetProductById")
    public ApiResponse<?> GetProductById(@RequestParam("product_id") int product_id) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.GetProductById(product_id));
        return apiResponse;
    }

    @GetMapping("/findProductByNameorCode")
    public ApiResponse<?> getProductByNameorCodelCategory(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_by_Name_Or_Code";
//        List<Products> products;
//        String cachedData = jedis.hget(cacheKey, key);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Products>>() {
//            }.getType();
//
//            products = gson.fromJson(cachedData, type);
//        } else {
//            products = productService.findProductByNameCode(key);
//            String jsonData = gson.toJson(products);
//            jedis.hset(cacheKey, key, jsonData);
//            jedis.expire(cacheKey, 1800);
//        }
        apiResponse.setResult(productService.findProductByNameCode(key));
//        apiResponse.setResult(productService.findProductByNameCode(key));
        return apiResponse;

    }

    @GetMapping("/GetAllCategory")
    public ApiResponse<?> getAllCategory() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_categories";
//        List<Categories> categories;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Categories>>() {
//            }.getType();
//
//            categories = gson.fromJson(cachedData, type);
//        } else {
//            categories = categoryRepository.findAll();
//            String jsonData = gson.toJson(categories);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1800);
//        }
        apiResponse.setResult(categoryRepository.findAll());
//        apiResponse.setResult(categoryRepository.findAll());
        return apiResponse;
    }

    @GetMapping("/getAllCategoryName")
    public ApiResponse<?> getAllCategoryName() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_categories_name";
//        List<CategoryNameDTO> categories;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<CategoryNameDTO>>() {
//            }.getType();
//
//            categories = gson.fromJson(cachedData, type);
//        } else {
//            categories = categorySevice.GetListName();
//            String jsonData =gson.toJson(categories);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1800);
//        }
        apiResponse.setResult(categorySevice.GetListName());
//        apiResponse.setResult(categorySevice.GetListName());
        return apiResponse;
    }






    //    @PostMapping(value = "/AddNewProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponse<?> AddNewProduct(
//            @RequestPart("productDTO") ProductDTO productDTO,
//            @RequestPart("files") MultipartFile[] files,
//            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
//    ) {
//        ApiResponse<Products> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(productService.AddNewProduct(productDTO, files, file_thumbnail));
//        return apiResponse;
//    }
//    @PostMapping(value = "/AddNewProduct")
//    public ApiResponse<?> AddNewProduct(
//            @RequestBody ProductAddDTO productAddDTO
//    ) {
//        ApiResponse<Products> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(productService.AddNewProduct(productAddDTO));
//        jedis.del("all_products");
//        jedis.del("all_products_by_status");
//        jedis.del("all_products_by_cate");
//        return apiResponse;
//    }
    @PostMapping(value = "/AddNewProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewProduct(
            @RequestPart("productDTO") ProductAddDTO productAddDTO,
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
    ) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        jedis.del("all_products_admin");
        jedis.del("all_products_customer");
        apiResponse.setResult(productService.AddNewProduct(productAddDTO, files, file_thumbnail));
        return apiResponse;
    }

//    @PostMapping("/upload")
//    public ResponseEntity<Object> uploadImage(@RequestParam("files") MultipartFile[] files, @RequestParam("product_id") int product_id) {
//        try {
//            return ResponseEntity.ok().body(uploadImageService.uploadFile(files, product_id));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
//        }
//    }

    @GetMapping("/upload")
    public String uploadFile() {
        // Lấy tên tệp tin gốc

        // Tạo đường dẫn tuyệt đối đến thư mục Images
        String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
        String absoluteUploadPath = projectDir + "/src/main/java/com/example/demo/Images/";

        // Tạo đường dẫn tuyệt đối đến tệp tin sẽ được lưu
        return absoluteUploadPath;

    }


    @PutMapping(value = "/EditProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> EditProduct(
            @RequestParam(value="product_id") int productId,
            @RequestPart("productDTO") ProductEditDTO productEditDTO,
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
    ) throws Exception {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        jedis.del("all_sub_mate_product");
        jedis.del("all_sub_mate_re_product");
        jedis.del("all_products_customer");
        jedis.del("all_products_admin");
        apiResponse.setResult(productService.EditProduct(productId,productEditDTO,files, file_thumbnail));
        return apiResponse;
    }

    @PutMapping(value = "/EditRequestProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> EditRequestProduct(
            @RequestParam(value="product_id") int productId,
            @RequestPart("productDTO") RequestProductEditDTO requestProductEditDTO,
            @RequestPart("files") MultipartFile[] files
    ) throws Exception {
        ApiResponse<RequestProducts> apiResponse = new ApiResponse<>();
        jedis.del("all_sub_mate_product");
        jedis.del("all_products_customer");
        jedis.del("all_sub_mate_re_product");
        jedis.del("all_products_admin");
        apiResponse.setResult(productService.EditRequestProduct(productId,requestProductEditDTO,files));
        return apiResponse;
    }

    @DeleteMapping("/deleteimages")
    public ResponseEntity<?> deleteImage(@RequestParam("id") String id) {
        try {
            Map result = cloudinaryService.deleteImage(id);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting image");
        }
    }

    @DeleteMapping("/deleteProduct")
    public ApiResponse<?> deleteProduct(@RequestParam("id") int product_id) {
      ApiResponse<String> apiResponse = new ApiResponse<>();
      productService.DeleteProduct(product_id);
        jedis.del("all_products_customer");
      apiResponse.setResult("Xoá thành công");
      return apiResponse;
    }

    @DeleteMapping("/deleteRequestProduct")
    public ApiResponse<?> deleteRequestProduct(@RequestParam("id") int product_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        productService.DeleteRequestProduct(product_id);
        apiResponse.setResult("Xoá thành công");
        return apiResponse;
    }



    //edit chỗ status product thì chỉ cho chọn là hết hàng hay là còn hàng , nếu còn hàng thì show ra cho customer xem trên landingpage
//    @PutMapping(value = "/EditProduct")
//    public ApiResponse<?> EditProduct(
//            @RequestParam(value = "product_id") int productId,
//            @RequestBody ProductEditDTO productEditDTO
//    ) {
//        ApiResponse<Products> apiResponse = new ApiResponse<>();
//
//        apiResponse.setResult(productService.EditProduct(productId, productEditDTO));
//        jedis.del("all_products");
//        jedis.del("all_products_by_status");
//        jedis.del("all_products_by_cate");
//        return apiResponse;
//    }



    @GetMapping("/GetStatusProduct")
    public ApiResponse<?> GetAllStatusProduct() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statusProductRepository.GetListStatusType());
        return apiResponse;
    }




    // neu input cua sortDirection la asc thi la sap xep tang dan` va desc la giam dan
    @GetMapping("/getMultiFillterProductForCustomer")
    public ApiResponse<?> getAllProductForCustomer(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortDirection){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String searchTerm = search == null ? "" : search.trim();

        apiResponse.setResult(productService.filterProductForCustomer(searchTerm, categoryId, minPrice, maxPrice, sortDirection));
        return apiResponse;
    }

    @GetMapping("/getMultiFillterProductForAdmin")
    public ApiResponse<?>  getAllProductForAdmin(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortDirection){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        jedis.del("all_products_customer");
        jedis.del("all_sub_mate_product");
        jedis.del("all_sub_mate_re_product");
        jedis.del("all_products_admin");
        String searchTerm = search == null ? "" : search.trim();

        apiResponse.setResult(productService.filterProductsForAdmin(searchTerm, categoryId, statusId, minPrice, maxPrice, sortDirection));
        return apiResponse;

    }

    @GetMapping("/getMultiFillterRequestProductForAdmin")
    public ApiResponse<?> getAllRequestProductForAdmin(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortDirection
         ){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        jedis.del("all_sub_mate_product");
        jedis.del("all_sub_mate_re_product");
        jedis.del("all_products_admin");
        String searchTerm = search == null ? "" : search.trim();

        apiResponse.setResult(orderService.filterRequestProductsForAdmin(searchTerm, statusId, minPrice, maxPrice, sortDirection));
        return apiResponse;
    }


    @GetMapping("/getProductSubMaterialAndMaterialByProductId")
    public ApiResponse<?> getProductSubMaterialAndMaterialByProductId(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_sub_mate_product";

        jedis.del("all_sub_mate_product");
        List<SubMateProductDTO> products;
        String cachedData = jedis.hget(cacheKey, id + "");
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<SubMateProductDTO>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productService.getProductSubMaterialByProductIdDTO(id);
            String jsonData = gson.toJson(products);
            jedis.hset(cacheKey, id + "", jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
        return apiResponse;
    }

    @GetMapping("/getRequestProductSubMaterialAndMaterialByRequestProductId")
    public ApiResponse<?> getRequestProductSubMaterialAndMaterialByRequestProductId(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_sub_mate_re_product";
        List<SubMateProductRequestDTO> products;
        String cachedData = jedis.hget(cacheKey, id + "");
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<SubMateProductRequestDTO>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productService.getRequestProductSubMaterialByRequestProductIdDTO(id);
            String jsonData = gson.toJson(products);
            jedis.hset(cacheKey, id + "", jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
        return apiResponse;
    }


    @PostMapping("/AddNewCategory")
    public ApiResponse<?> AddNewCategory(@RequestBody CategoryNameDTO categoryNameDTO) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        categorySevice.AddnewCategory(categoryNameDTO);
        jedis.del("all_categories");
        jedis.del("all_categories_name");
        apiResponse.setResult("Thêm mới Loại Sản Phẩm Thành công");
        return apiResponse;
    }

    @PutMapping("/EditCategory")
    public ApiResponse<?> EditCategory(@RequestParam("cate_id") int cate_id, @RequestBody CategoryNameDTO categoryNameDTO) {
        ApiResponse<Categories> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categorySevice.UpdateCategoty(cate_id, categoryNameDTO));
        jedis.del("all_categories");
        jedis.del("all_categories_name");
        return apiResponse;
    }
    @DeleteMapping("/DeleteCategory")
    public ApiResponse<?> DeleteCategory(@RequestParam("category_id")int category_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        categorySevice.DeleteCategory(category_id);
        jedis.del("all_categories");
        jedis.del("all_categories_name");
        apiResponse.setResult("Xoá Loại Sản Phẩm Thành công");
        return apiResponse;
    }
    @GetMapping("/findCategoriesByName")
    public ApiResponse<?> findCategoriesByName(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categorySevice.findCategoriesByName(key));
        jedis.del("all_categories");
        jedis.del("all_categories_name");
        return apiResponse;
    }


}
