package com.example.demo.Controllers.Product;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPooled;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/auth/product/")
@CrossOrigin(origins = "http://localhost:5173")
@AllArgsConstructor
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategorySevice categorySevice;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private WhiteListService whiteListService;
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



        apiResponse.setResult(productService.GetAllProductForCustomer());
        return apiResponse;
    }
    @GetMapping("/getAllProductForAdmin")
    public ApiResponse<?> getAllProductForAdmin() {
        ApiResponse<List> apiResponse = new ApiResponse<>();

        String cacheKey = "all_products_admin";
        List<Products> products;
        String cachedData = jedis.get(cacheKey);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Products>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productService.GetAllProductForAdmin();
            String jsonData = gson.toJson(products);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1200);
        }
      apiResponse.setResult(productService.GetAllProductForAdmin());

        return apiResponse;
    }


    @GetMapping("/GetProductByStatus")
    public ApiResponse<?> GetProductByStatus(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_products_by_status";
        List<Products> products;
        String cachedData = jedis.hget(cacheKey, id + "");
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Products>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productRepository.findByStatus(id);
            String jsonData = gson.toJson(products);
            jedis.hset(cacheKey, id + "", jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
//        apiResponse.setResult(productRepository.findByStatus(id));
        return apiResponse;
    }

    @GetMapping("/GetProductByCategory")
    public ApiResponse<?> GetProductByCategory(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_products_by_cate";
        List<Products> products;
        String cachedData = jedis.hget(cacheKey, id + "");
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Products>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productRepository.findByCategory(id);
            String jsonData = gson.toJson(products);
            jedis.hset(cacheKey, id + "", jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
//        apiResponse.setResult(productRepository.findByCategory(id));
        return apiResponse;
    }

    @PutMapping("/UpdateStatusProduct")
    public ApiResponse<?> UpdateStatusProduct(@RequestParam("product_id") int product_id, @RequestParam("status_id") int status_id) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.UpdateStatusProduct(product_id, status_id));
        jedis.del("all_products");
        jedis.del("all_products_by_status");
        jedis.del("all_products_by_cate");
        return apiResponse;
    }

    @GetMapping("/ViewDetailProductById")
    public ApiResponse<?> GetProductByIdWithImage(@RequestParam("id") int id) {
        ApiResponse<ProductDTO_Show> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.GetProductByIdWithImage(id));
        return apiResponse;
    }

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
        String cacheKey = "all_products_by_Name_Or_Code";
        List<Products> products;
        String cachedData = jedis.hget(cacheKey, key);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Products>>() {
            }.getType();

            products = gson.fromJson(cachedData, type);
        } else {
            products = productService.findProductByNameCode(key);
            String jsonData = gson.toJson(products);
            jedis.hset(cacheKey, key, jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(products);
//        apiResponse.setResult(productService.findProductByNameCode(key));
        return apiResponse;

    }

    @GetMapping("/GetAllCategory")
    public ApiResponse<?> getAllCategory() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_categories";
        List<Categories> categories;
        String cachedData = jedis.get(cacheKey);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Categories>>() {
            }.getType();

            categories = gson.fromJson(cachedData, type);
        } else {
            categories = categoryRepository.findAll();
            String jsonData = gson.toJson(categories);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(categories);
//        apiResponse.setResult(categoryRepository.findAll());
        return apiResponse;
    }

    @GetMapping("/getAllCategoryName")
    public ApiResponse<?> getAllCategoryName() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_categories_name";
        List<CategoryNameDTO> categories;
        String cachedData = jedis.get(cacheKey);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<CategoryNameDTO>>() {
            }.getType();

            categories = gson.fromJson(cachedData, type);
        } else {
            categories = categorySevice.GetListName();
            String jsonData =gson.toJson(categories);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1800);
        }
        apiResponse.setResult(categories);
//        apiResponse.setResult(categorySevice.GetListName());
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
    ) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();

        apiResponse.setResult(productService.EditProduct(productId,productEditDTO,files, file_thumbnail));
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

    //xuất đơn nguyên vật liệu cho product có sẵn
    @PostMapping("/createExportMaterialProduct")
    public ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(@RequestBody CreateExportMaterialProductRequest request) {
        return productService.createExportMaterialProduct(request.getProductId(), request.getSubMaterialQuantities());
    }

    //xuất đơn vật liệu cho đơn hàng đặt theo yêu cầu , request product
    @PostMapping("/createExportMaterialProductRequest")
    public ResponseEntity<ApiResponse<List<RequestProductsSubmaterials>>> createExportMaterialProductRequest(@RequestBody CreateExportMaterialProductRequest request) {
        return productService.createExportMaterialProductRequest(request.getProductId(), request.getSubMaterialQuantities());
    }

    @GetMapping("/GetStatusProduct")
    public ApiResponse<?> GetAllStatusProduct() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statusProductRepository.GetListStatusType0());
        return apiResponse;
    }


    // neu input cua sortDirection la asc thi la sap xep tang dan` va desc la giam dan
    @GetMapping("/getMultiFillterProductForCustomer")
    public ResponseEntity<?> getAllProductForCustomer(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortDirection){
        List<Products> products = productService.GetAllProductForCustomer(search, categoryId, minPrice, maxPrice, sortDirection);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/getMultiFillterProductForAdmin")
    public ResponseEntity<?> getAllProductForAdmin(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String sortDirection){
        List<Products> products = productService.filterProductsForAdmin(search, categoryId, statusId, minPrice, maxPrice, sortDirection);
        return ResponseEntity.ok(products);
    }

}
