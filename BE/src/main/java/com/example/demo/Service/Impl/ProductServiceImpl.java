package com.example.demo.Service.Impl;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UploadImageService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    @Autowired
    private Status_Product_Repository statusRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private SubMaterialsRepository subMaterialsRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private EntityManager entityManager;
    @Override
    public Products AddNewProduct(ProductDTO1 productDTO1){
        Products products = new Products();
        // Chuyển đổi completion_time sang java.sql.Date
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date
        products.setCompletionTime(sqlCompletionTime);

        // Tính toán EndDateWarranty (2 năm sau thời điểm hiện tại)
        LocalDate endDateWarranty = currentDate.plusYears(2);
        java.sql.Date sqlEndDateWarranty = java.sql.Date.valueOf(endDateWarranty);
        products.setEnddateWarranty(sqlEndDateWarranty);

        products.setProductName(productDTO1.getProduct_name());
        products.setDescription(productDTO1.getDescription());
        products.setPrice(productDTO1.getPrice());

        Status_Product status = statusRepository.findById(productDTO1.getStatus_id());
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productDTO1.getCategory_id());
        products.setCategories(categories);
        products.setType(productDTO1.getType());

        products.setQuantity(productDTO1.getQuantity());
        if (productRepository.countByProductName(productDTO1.getProduct_name()) > 0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }

        validateProductDTO1(productDTO1);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Products lastProduct = productRepository.findProductTop(dateString + "PD");
        int count = lastProduct != null ? Integer.parseInt(lastProduct.getCode().substring(8)) + 1 : 1;
        String code = dateString + "PD" + String.format("%03d", count);
        products.setCode(code);
        //set ảnh thumbnail
        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(productDTO1.getFile_thumbnail());
        products.setImage(t.getFullPath());
        products = productRepository.save(products);
        //set ảnh của product
        Products product = productRepository.findByName(productDTO1.getProduct_name());
        uploadImageService.uploadFile(productDTO1.getFiles(), product.getProductId());
        return products;
    }


//    @Override
//    public Products AddNewProduct(ProductDTO productDTO, MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) {
//        Products products = new Products();
//        // Chuyển đổi completion_time sang java.sql.Date
//        LocalDate currentDate = LocalDate.now();
//        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date
//        products.setCompletionTime(sqlCompletionTime);
//
//        // Tính toán EndDateWarranty (2 năm sau thời điểm hiện tại)
//        LocalDate endDateWarranty = currentDate.plusYears(2);
//        java.sql.Date sqlEndDateWarranty = java.sql.Date.valueOf(endDateWarranty);
//        products.setEnddateWarranty(sqlEndDateWarranty);
//
//        products.setProductName(productDTO.getProduct_name());
//        products.setDescription(productDTO.getDescription());
//        products.setPrice(productDTO.getPrice());
//
//        Status_Product status = statusRepository.findById(productDTO.getStatus_id());
//        products.setStatus(status);
//        Categories categories = categoryRepository.findById(productDTO.getCategory_id());
//        products.setCategories(categories);
//        products.setType(productDTO.getType());
//
//        products.setQuantity(productDTO.getQuantity());
//        if (productRepository.countByProductName(productDTO.getProduct_name()) > 0) {
//            throw new AppException(ErrorCode.NAME_EXIST);
//        }
//
//        validateProductDTO(productDTO);
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
//        String dateString = today.format(formatter);
//
//        Products lastProduct = productRepository.findProductTop(dateString + "PD");
//        int count = lastProduct != null ? Integer.parseInt(lastProduct.getCode().substring(8)) + 1 : 1;
//        String code = dateString + "PD" + String.format("%03d", count);
//        products.setCode(code);
//        //set ảnh thumbnail
//        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
//        products.setImage(t.getFullPath());
//        products = productRepository.save(products);
//        //set ảnh của product
//        Products product = productRepository.findByName(productDTO.getProduct_name());
//        uploadImageService.uploadFile(multipartFiles, product.getProductId());
//        return products;
//    }

    @Transactional
    @Override
    public Products EditProduct(int id, ProductDTO1 productDTO1) {
        Products products = productRepository.findById(id);
        String thumbnailPath = products.getImage(); // Lấy đường dẫn thumbnail hiện tại

        if (productDTO1.getFile_thumbnail() != null && !productDTO1.getFile_thumbnail().isEmpty()) {
            //set ảnh thumbnail
            Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(productDTO1.getFile_thumbnail());
            thumbnailPath = t.getFullPath(); // Cập nhật nếu có ảnh mới
        }
        if (productDTO1.getFiles() != null && !productDTO1.getFiles().isEmpty()) {
            productImageRepository.deleteProductImages(id); // Xóa những ảnh trước đó
            uploadImageService.uploadFile(productDTO1.getFiles(), products.getProductId());
        }
        // Kiểm tra tên sản phẩm trước khi cập nhật
        if (!productDTO1.getProduct_name().equals(products.getProductName()) &&
                productRepository.findByName(productDTO1.getProduct_name()) != null) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        validateProductDTO1(productDTO1);
        productRepository.updateProduct(id,
                productDTO1.getProduct_name(),
                productDTO1.getDescription(),
                productDTO1.getQuantity(),
                productDTO1.getPrice(),
                productDTO1.getStatus_id(),
                productDTO1.getCategory_id(),
                productDTO1.getType(),
                thumbnailPath, // Sử dụng thumbnailPath đã cập nhật
                productDTO1.getCompletionTime(),
                productDTO1.getEnddateWarranty()
        );
        entityManager.refresh(products); // Làm mới đối tượng products
return products;
    }

    @Transactional
    @Override
    public  Products UpdateStatusProduct(int product_id, int status_id){
        productRepository.updateStatus(product_id,status_id);
        return productRepository.findById(product_id);
    }



    @Override
    public ProductDTO_Show GetProductByIdWithImage(int id) {
        List<Productimages> productimagesList = productImageRepository.findImageByProductId(id);
        Products products = productRepository.findById(id);
        ProductDTO_Show productDTOShow = new ProductDTO_Show();
        productDTOShow.setProductId(products.getProductId());
        productDTOShow.setProductName(products.getProductName());
        productDTOShow.setDescription(products.getDescription());
        productDTOShow.setPrice(products.getPrice());
        productDTOShow.setImage(getAddressLocalComputer(products.getImage()));
        productDTOShow.setCompletionTime(products.getCompletionTime());
        productDTOShow.setEnddateWarranty(products.getEnddateWarranty());
        productDTOShow.setCode(products.getCode());
        productDTOShow.setType(products.getType());
        productDTOShow.setStatus(products.getStatus());
        productDTOShow.setCategories(products.getCategories());
        List<Productimages> processedImages = new ArrayList<>(); // Danh sách mới
        for(Productimages productimages : productimagesList){
            productimages.setFullPath(getAddressLocalComputer(productimages.getFullPath()));
            processedImages.add(productimages); // Thêm vào danh sách mới
        }
        productDTOShow.setImageList(processedImages); // Gán danh sách mới vào DTO

        return productDTOShow;

    }

    private String getAddressLocalComputer(String imagePath) {
        int assetsIndex = imagePath.indexOf("/assets/");
        if (assetsIndex != -1) {
            imagePath = imagePath.substring(assetsIndex); // Cắt từ "/assets/"
            if (imagePath.startsWith("/")) { // Kiểm tra xem có dấu "/" ở đầu không
                imagePath = imagePath.substring(1); // Loại bỏ dấu "/" đầu tiên
            }
        }
        return imagePath;
    }// Trả về đường dẫn tương đối hoặc đường dẫn ban đầu nếu không tìm thấy "/assets/"


    @Override
    public List<Products> GetAllProduct() {
        List<Products> productList = productRepository.findAll();
        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        for (Products product : productList) {
            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
        }
        return productList;
    }



    @Override
    public Products GetProductById(int product_id){
        Products products = productRepository.findById(product_id);
      //  String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
        products.setImage(getAddressLocalComputer(products.getImage()));

        return products;
    }




    @Override
    public List<Products> findProductByNameCode(String key) {
        List<Products> productsList = productRepository.findProductByNameCode(key);
        if(productsList.size() ==0){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        for(Products products :productsList) {

            //  String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
            products.setImage(getAddressLocalComputer(products.getImage()));

        }
        return productsList;
    }




    // Hàm kiểm tra điều kiện đầu vào
    private void validateProductDTO(ProductDTO productDTO) {
        if (!checkConditionService.checkInputName(productDTO.getProduct_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (!checkConditionService.checkInputQuantity(productDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(productDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }
    private void validateProductDTO1(ProductDTO1 productDTO1) {
        if (!checkConditionService.checkInputName(productDTO1.getProduct_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (!checkConditionService.checkInputQuantity(productDTO1.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(productDTO1.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }


    //Đơn xuất vật liệu(Tạo đơn xuất vật liệu cho sản phẩm  UC_30)
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int productId, Map<Integer, Integer> subMaterialQuantities) {
        Products product = productRepository.findById(productId);

        List<ProductSubMaterials> productSubMaterialsList = new ArrayList<>();
        Map<String, String> errors = new HashMap<>(); //hashmap cho error

        for (Map.Entry<Integer, Integer> entry : subMaterialQuantities.entrySet()) {
            int subMaterialId = entry.getKey();
            int quantity = entry.getValue();
            SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);

            int currentQuantity = subMaterial.getQuantity();
            if (quantity > currentQuantity) {
                errors.put(subMaterial.getSubMaterialName(), "Không đủ số lượng");
                continue;
            }

            subMaterial.setQuantity(currentQuantity - quantity);
            subMaterialsRepository.save(subMaterial);

            ProductSubMaterials productSubMaterial = new ProductSubMaterials(subMaterial, product, quantity);
            productSubMaterialsList.add(productSubMaterial);
        }
        ApiResponse<List<ProductSubMaterials>> apiResponse = new ApiResponse<>();
        if (!errors.isEmpty()) {
            apiResponse.setError(1028, errors);
            return ResponseEntity.badRequest().body(apiResponse);
        } else {
            apiResponse.setResult(productSubMaterialsRepository.saveAll(productSubMaterialsList));
            return ResponseEntity.ok(apiResponse);
        }
    }
    //Đơn tạo đơn xuất vật liệu cho Employee



}