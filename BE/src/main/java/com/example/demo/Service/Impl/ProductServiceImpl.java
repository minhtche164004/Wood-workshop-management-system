package com.example.demo.Service.Impl;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO_Show;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UploadImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private UserRepository userRepository;
    @Autowired
    private Status_Request_Repository statusRequestRepository;

    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private SubMaterialsRepository subMaterialsRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private RequestRepository requestRepository;


    @Override
    public Products AddNewProduct(ProductDTO productDTO, MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) {
        Products products = new Products();
        // Chuyển đổi completion_time sang java.sql.Date
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date
        products.setCompletionTime(sqlCompletionTime);

        // Tính toán EndDateWarranty (2 năm sau thời điểm hiện tại)
        LocalDate endDateWarranty = currentDate.plusYears(2);
        java.sql.Date sqlEndDateWarranty = java.sql.Date.valueOf(endDateWarranty);
        products.setEnddateWarranty(sqlEndDateWarranty);

        products.setProductName(productDTO.getProduct_name());
        products.setDescription(productDTO.getDescription());
        products.setPrice(productDTO.getPrice());

        Status_Product status = statusRepository.findById(productDTO.getStatus_id());
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productDTO.getCategory_id());
        products.setCategories(categories);
        products.setType(productDTO.getType());

        products.setQuantity(productDTO.getQuantity());
        if (productRepository.countByProductName(productDTO.getProduct_name()) > 0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }

        validateProductDTO(productDTO);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Products lastProduct = productRepository.findProductTop(dateString + "PD");
        int count = lastProduct != null ? Integer.parseInt(lastProduct.getCode().substring(8)) + 1 : 1;
        String code = dateString + "PD" + String.format("%03d", count);
        products.setCode(code);
        //set ảnh thumbnail
        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
        products.setImage(t.getFullPath());
        products = productRepository.save(products);
        //set ảnh của product
        Products product = productRepository.findByName(productDTO.getProduct_name());
        uploadImageService.uploadFile(multipartFiles, product.getProductId());
        return products;
    }

    @Override
    public Products EditProduct(int id, ProductDTO productDTO, MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) {
        Products products = productRepository.findById(id);
        //set ảnh thumbnail
        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
        uploadImageService.uploadFile(multipartFiles, products.getProductId());
        // Kiểm tra tên sản phẩm trước khi cập nhật
        if (!productDTO.getProduct_name().equals(products.getProductName()) &&
                productRepository.findByName(productDTO.getProduct_name()) != null) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        validateProductDTO(productDTO);
        productRepository.updateProduct(id,
                productDTO.getProduct_name(),
                productDTO.getDescription(),
                productDTO.getQuantity(),
                productDTO.getPrice(),
                productDTO.getStatus_id(),
                productDTO.getCategory_id(),
                productDTO.getType(),
                t.getFullPath(),
                productDTO.getCompletionTime(),
                productDTO.getEnddateWarranty()
        );
        return products;
    }


//    @Override
//    public List<ProductDTO_Show> GetAllProduct() {
//        String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
//        List<Products> product_list = productRepository.findAll();
//        if (product_list.isEmpty()) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return product_list.stream()
//                .map(product -> {
//                    ProductDTO_Show productDTO = modelMapper.map(product, ProductDTO_Show.class);
//                    productDTO.setImages( projectDir + productDTO.getImages());
//                    return productDTO;
//                })
//                .collect(Collectors.toList());
//    }
    private String getAddressLocalComputer(){
        String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
        Path projectDirPath = Paths.get(projectDir);
        Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha(ko có /BE)
        String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
        return desiredPath;
    }


    @Override
    public List<Products> GetAllProduct() {
        List<Products> product_list = productRepository.findAll();
        if (product_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        for(Products product : product_list) {
            product.setImage(getAddressLocalComputer() + product.getImage());
        }
       return product_list;
    }

    @Override
    public Products GetProductById(int product_id){
        Products products = productRepository.findById(product_id);
      //  String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
        products.setImage(getAddressLocalComputer() + products.getImage());
        return products;
    }


    //Tạo Request Product
    @Override
    public RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles) {
        RequestProducts requestProducts = new RequestProducts();
        requestProducts.setRequestProductName(requestProductDTO.getRequestProductName());
        requestProducts.setDescription(requestProductDTO.getDescription());
        requestProducts.setPrice(requestProductDTO.getPrice());
        requestProducts.setQuantity(requestProductDTO.getQuantity());
        requestProducts.setCompletionTime(requestProductDTO.getCompletionTime());
        if (!checkConditionService.checkInputName(requestProductDTO.getRequestProductName())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
//        if (requestProductRepository.countByRequestProductName(requestProductDTO.getRequestProductName()) > 0) {
//            throw new AppException(ErrorCode.NAME_EXIST);
//        }
        if (!checkConditionService.checkInputQuantity(requestProductDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(requestProductDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
        requestProducts = requestProductRepository.save(requestProducts);
//        //set ảnh thumbnail
//        Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
//        requestProducts.setImage(t.getFullPath());
        //set ảnh của product
        RequestProducts requestProduct = requestProductRepository.findByName(requestProductDTO.getRequestProductName());
        uploadImageService.uploadFile1(multipartFiles, requestProduct.getRequestProductId());
        return requestProducts;
    }
    @Override
    public List<RequestProducts> GetAllProductRequest() {
        List<RequestProducts> reproduct_list = requestProductRepository.findAll();
        if (reproduct_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return reproduct_list;
    }

    @Override
    public List<Requests> GetAllRequests() {
        List<Requests> request_list = requestRepository.findAll();
        if (request_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return request_list;
    }

    @Override
    public List<Products> findProductByNameCode(String key) {
        List<Products> productsList = productRepository.findProductByNameCode(key);
        for(Products products :productsList){
          //  String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
            products.setImage(getAddressLocalComputer() + products.getImage());
        }
        return productsList;
    }


    //Tạo Request
    //Tạo Request Product
    @Override
    public Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles) {
        Requests requests = new Requests();
        User user = userRepository.findById(requestDTO.getUser_id()).get();
        requests.setUser(user);
        Status_Request statusRequest =statusRequestRepository.findById(requestDTO.getStatus_id()).get();
        requests.setRequestDate(requestDTO.getRequestDate());
        requests.setDescription(requestDTO.getDescription());
        requests.setStatus(statusRequest);
        requests.setAddress(requestDTO.getAddress());
        requests.setFullname(requestDTO.getFullname());
        requests.setPhoneNumber(requestDTO.getPhoneNumber());
        requests.setResponse(requestDTO.getResponse());

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Requests lastRequest = requestRepository.findRequestTop(dateString + "RQ");
        int count = lastRequest != null ? Integer.parseInt(lastRequest.getCode().substring(8)) + 1 : 1;
        String code = dateString + "RQ" + String.format("%03d", count);
        requests.setCode(code);

        if (!checkConditionService.checkInputName(requestDTO.getFullname())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }

        requests = requestRepository.save(requests);
        uploadImageService.uploadFile2(multipartFiles, requests.getRequestId());
        return requests;
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