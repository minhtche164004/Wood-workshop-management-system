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
    @Autowired
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Autowired
    private RequestimagesRepository requestimagesRepository;
    @Autowired
    private ProductImageRepository productImageRepository;


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
@Override
public List<RequestProducts> GetAllProductRequest() {
    return requestProductRepository.findAll();
}
    private String getAddressLocalComputer(String imagePath){
        int assetsIndex = imagePath.indexOf("/assets/");
        if (assetsIndex != -1) {
            imagePath = imagePath.substring(assetsIndex); // Cắt từ "/assets/"
            if (imagePath.startsWith("/")) { // Kiểm tra xem có dấu "/" ở đầu không
                imagePath = imagePath.substring(1); // Loại bỏ dấu "/" đầu tiên
            }
        }
        return imagePath; // Trả về đường dẫn tương đối hoặc đường dẫn ban đầu nếu không tìm thấy "/assets/"
    }


    @Override
    public RequestProductAllDTO GetProductRequestById(int id) {
        List<Product_Requestimages> productRequestimagesList = productRequestimagesRepository.findById(id);
        RequestProducts requestProducts = requestProductRepository.findById(id);
        RequestProductAllDTO requestProductAllDTO = new RequestProductAllDTO();
        requestProductAllDTO.setId(requestProducts.getRequestProductId());
        requestProductAllDTO.setRequest_id(requestProducts.getRequestProductId());
        requestProductAllDTO.setQuantity(requestProducts.getQuantity());
        requestProductAllDTO.setPrice(requestProducts.getPrice());
        requestProductAllDTO.setCompletionTime(requestProducts.getCompletionTime());
        requestProductAllDTO.setDescription(requestProducts.getDescription());
        List<Product_Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for(Product_Requestimages productRequestimages : productRequestimagesList){
            productRequestimages.setFullPath(getAddressLocalComputer(productRequestimages.getFullPath()));
            processedImages.add(productRequestimages); // Thêm vào danh sách mới
        }
        requestProductAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestProductAllDTO;
    }

    @Override
    public RequestAllDTO GetRequestById(int id) {
        List<Requestimages> requestimagesList = requestimagesRepository.findById(id);
        Requests requests = requestRepository.findById(id);
        RequestAllDTO requestAllDTO = new RequestAllDTO();
        requestAllDTO.setUser_id(requests.getUser().getUserId());
        requestAllDTO.setRequestDate(requests.getRequestDate());
        requestAllDTO.setResponse(requests.getResponse());
        requestAllDTO.setPhoneNumber(requests.getPhoneNumber());
        requestAllDTO.setFullname(requests.getFullname());
        requestAllDTO.setAddress(requests.getAddress());
        requestAllDTO.setCity_province(requests.getCity_province());
        requestAllDTO.setStatus_id(requests.getStatus().getStatus_id());
        requestAllDTO.setDistrict(requests.getDistrict());
        requestAllDTO.setWards(requests.getWards());
        requestAllDTO.setDescription(requests.getDescription());
        List<Requestimages> processedImages = new ArrayList<>(); // Danh sách mới
        for(Requestimages requestimages : requestimagesList){
            requestimages.setFullPath(getAddressLocalComputer(requestimages.getFullPath()));
            processedImages.add(requestimages); // Thêm vào danh sách mới
        }
        requestAllDTO.setImagesList(processedImages); // Gán danh sách mới vào DTO

        return requestAllDTO;
    }



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


    //Tạo Request Product
    @Override
    public RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles) { //lấy từ request
        RequestProducts requestProducts = new RequestProducts();
        requestProducts.setRequestProductName(requestProductDTO.getRequestProductName());
        requestProducts.setDescription(requestProductDTO.getDescription());
        requestProducts.setPrice(requestProductDTO.getPrice());
        requestProducts.setQuantity(requestProductDTO.getQuantity());
        requestProducts.setCompletionTime(requestProductDTO.getCompletionTime());
        Requests requests = requestRepository.findById(requestProductDTO.getRequest_id());
        requestProducts.setRequests(requests);
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
            products.setImage(getAddressLocalComputer(products.getImage()));
        }
        return productsList;
    }


    //Tạo Request
    //Tạo Request Product
    @Override
    public Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles) {
        Requests requests = new Requests();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =userRepository.getUserByUsername(userDetails.getUsername());
        //lấy thông tin thằng đang login
     //   User user = userRepository.findById(requestDTO.getUser_id()).get();
        requests.setUser(user);
        Status_Request statusRequest =statusRequestRepository.findById(1).get();//nghĩa là request đang chờ phê duyệt
        requests.setRequestDate(requestDTO.getRequestDate());
        requests.setDescription(requestDTO.getDescription());
        requests.setStatus(statusRequest);
        requests.setAddress(requestDTO.getAddress());
        requests.setFullname(requestDTO.getFullname());
        requests.setPhoneNumber(requestDTO.getPhoneNumber());
        requests.setResponse("");
        requests.setCity_province(requestDTO.getCity_province());
        requests.setDistrict(requestDTO.getDistrict());
        requests.setWards(requestDTO.getWards());
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
    @Override
    public Requests getRequestById(int id){
        return requestRepository.findById(id);
    }
    @Override
    public RequestProducts getRequestProductsById(int id){
        return requestProductRepository.findById(id);
    }
    @Transactional
    @Override
    public void Approve_Reject_Request(int id, int status_id){
        requestRepository.updateStatus(id,status_id);
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