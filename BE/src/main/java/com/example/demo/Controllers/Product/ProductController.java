package com.example.demo.Controllers.Product;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.List;

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
    private UploadImageService uploadImageService;
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/GetAllProduct")
    public ApiResponse<?> getAllProduct() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.GetAllProduct());
        return apiResponse;
    }
    @GetMapping("/GetProductByCategory")
    public ApiResponse<?> GetProductByCategory(@RequestParam("id") int id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productRepository.findByCategory(id));
        return apiResponse;
    }

    @GetMapping("/GetProductByIdWithImage")
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
        apiResponse.setResult(productService.findProductByNameCode(key));
        return apiResponse;

    }

    @GetMapping("/GetAllCategory")
    public ApiResponse<?> getAllCategory() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categoryRepository.findAll());
        return apiResponse;
    }

    @GetMapping("/getAllCategoryName")
    public ApiResponse<?> getAllCategoryName() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categorySevice.GetListName());
        return apiResponse;
    }
    @PostMapping("/AddNewCategory")
    public ApiResponse<?> AddNewCategory(@RequestBody CategoryNameDTO categoryNameDTO) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        categorySevice.AddnewCategory(categoryNameDTO);
        apiResponse.setResult("Thêm mới Loại Sản Phẩm Thành công");
        return apiResponse;
    }



    @PutMapping("/EditCategory")
    public ApiResponse<?> EditCategory(@RequestParam("cate_id") int cate_id,@RequestBody CategoryNameDTO categoryNameDTO) {
        ApiResponse<Categories> apiResponse = new ApiResponse<>();
        apiResponse.setResult(categorySevice.UpdateCategoty(cate_id,categoryNameDTO));
        return apiResponse;
    }

    @PostMapping(value = "/AddNewProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewProduct(
            @RequestPart("productDTO") ProductDTO productDTO,
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
    ) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.AddNewProduct(productDTO, files, file_thumbnail));
        return apiResponse;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImage(@RequestParam("files") MultipartFile[] files, @RequestParam("product_id") int product_id) {
        try {
            return ResponseEntity.ok().body(uploadImageService.uploadFile(files, product_id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

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
            @RequestPart("productDTO") ProductDTO productDTO,
            @RequestPart("files") MultipartFile[] files,
            @RequestPart("file_thumbnail") MultipartFile file_thumbnail
    ) {
        ApiResponse<Products> apiResponse = new ApiResponse<>();
        apiResponse.setResult(productService.EditProduct(productId,productDTO,files, file_thumbnail));
        return apiResponse;
    }

    @PostMapping("/createExportMaterialProduct")
    public ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(@RequestBody CreateExportMaterialProductRequest request) {
        return productService.createExportMaterialProduct(request.getProductId(), request.getSubMaterialQuantities());
    }


}
