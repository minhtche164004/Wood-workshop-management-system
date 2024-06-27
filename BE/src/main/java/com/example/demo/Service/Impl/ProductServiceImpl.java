package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.CloudinaryService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UploadImageService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private Status_Product_Repository statusProductRepository;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private ProcessproducterrorRepository processproducterrorRepository;
    @Autowired
    private WishListRepository wishListRepository;



    @Override
    public Products AddNewProduct(ProductAddDTO productAddDTO, MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) {

        Products products = new Products();
        // Chuyển đổi completion_time sang java.sql.Date
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date
        products.setCompletionTime(sqlCompletionTime);

        // Tính toán EndDateWarranty (2 năm sau thời điểm hiện tại)
        LocalDate endDateWarranty = currentDate.plusYears(2);
        java.sql.Date sqlEndDateWarranty = java.sql.Date.valueOf(endDateWarranty);
        products.setEnddateWarranty(sqlEndDateWarranty);

        products.setProductName(productAddDTO.getProduct_name());
        products.setDescription(productAddDTO.getDescription());
        products.setPrice(productAddDTO.getPrice());


        Status_Product status = statusProductRepository.findById(2);//tuc la kich hoạt
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productAddDTO.getCategory_id());

        products.setCategories(categories);
        products.setType(productAddDTO.getType());


        products.setQuantity(0);
//            if (productRepository.countByProductName(productAddDTO.getProduct_name()) > 0) {
//                    throw new AppException(ErrorCode.NAME_EXIST);
//                }

        validateProductDTO1(productAddDTO);

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
        Products product = productRepository.findByCode(products.getCode());
        uploadImageService.uploadFile(multipartFiles, product.getProductId());

        return products;
    }




    @Transactional
    @Override

    public Products EditProduct(int id, ProductEditDTO productEditDTO,MultipartFile[] multipartFiles, MultipartFile multipartFiles_thumbnal) throws IOException {
        Products products = productRepository.findById(id);
        String thumbnailPath = products.getImage(); // Lấy đường dẫn thumbnail hiện tại

        if (multipartFiles_thumbnal != null && !multipartFiles_thumbnal.isEmpty()) {
            //set ảnh thumbnail
            String id_image = cloudinaryService.extractPublicIdFromUrl(thumbnailPath);
            cloudinaryService.deleteImage(id_image);
            Product_Thumbnail t = uploadImageService.uploadFile_Thumnail(multipartFiles_thumbnal);
            thumbnailPath = t.getFullPath(); // Cập nhật nếu có ảnh mới


        }
        if (multipartFiles != null &&
                Arrays.stream(multipartFiles).anyMatch(file -> file != null && !file.isEmpty())) {
            List<Productimages> productimages= productImageRepository.findImageByProductId(id);
            for(Productimages productimages1 : productimages){
                String full_path= productimages1.getFullPath();
                String id_image =cloudinaryService.extractPublicIdFromUrl(full_path);
                cloudinaryService.deleteImage(id_image);
            }
            productImageRepository.deleteProductImages(id); // Xóa những ảnh trước đó
            uploadImageService.uploadFile(multipartFiles, products.getProductId());


        }
        //ko đc chỉnh sửa quantity
        validateProductEditDTO(productEditDTO);
        productRepository.updateProduct(id,
                productEditDTO.getProduct_name(),
                productEditDTO.getDescription(),
                productEditDTO.getPrice(),
                productEditDTO.getStatus_id(),
                productEditDTO.getCategory_id(),
                productEditDTO.getType(),
                thumbnailPath, // Sử dụng thumbnailPath đã cập nhật
                productEditDTO.getEnddateWarranty()
        );
        entityManager.refresh(products); // Làm mới đối tượng products
        return products;
    }

    @Transactional
    @Override
    public Products UpdateStatusProduct(int product_id, int status_id) {
        productRepository.updateStatus(product_id, status_id);
        return productRepository.findById(product_id);
    }

    @Override
    public void DeleteProduct(int product_id) {
        Products product = productRepository.findById(product_id);
        if(product == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        // Kiểm tra các ràng buộc
        if (orderDetailRepository.getOrderDetailByProductId(product_id).isEmpty() &&
                jobRepository.getJobByProductId(product_id).isEmpty() &&
                productImageRepository.findImageByProductId(product_id).isEmpty() &&
                processproducterrorRepository.getProcessproducterrorByProductId(product_id).isEmpty() &&
                wishListRepository.findByProductID(product_id).isEmpty() &&
                productSubMaterialsRepository.findByProductID(product_id).isEmpty()) {
            // Không có ràng buộc nào, có thể xóa sản phẩm
            productRepository.delete(product);
        } else {
            // Có ràng buộc, không thể xóa sản phẩm
            throw new AppException(ErrorCode.PRODUCT_HAS_RELATIONSHIPS); // Hoặc một mã lỗi phù hợp
        }
    }

    @Override
    public ProductDTO_Show GetProductByIdWithImage(int id) {
        List<Productimages> productimagesList = productImageRepository.findImageByProductId(id);
        Products products = productRepository.findById(id);
        if (products == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        ProductDTO_Show productDTOShow = new ProductDTO_Show();
        if (productimagesList == null) {
            productDTOShow.setImageList(null);
        }
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
        for (Productimages productimages : productimagesList) {
            productimages.setFullPath(getAddressLocalComputer(productimages.getFullPath()));
            processedImages.add(productimages); // Thêm vào danh sách mới
        }
        List<String> list = productSubMaterialsRepository.GetSubNameByProductId(id);
        if (list.isEmpty()) {
            productDTOShow.setSub_material_name(null);
        }
        productDTOShow.setSub_material_name(list);
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
    public List<Products> filterProductForCustomer(String search, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
        List<Products> productList = new ArrayList<>();

        if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
            productList = productRepository.filterProductsForCus(search, categoryId, minPrice, maxPrice);
        } else {
            productList = productRepository.ViewProductLandingPage();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        for (Products product : productList) {
            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
        }

        // Sắp xếp danh sách sản phẩm theo giá
        if (sortDirection != null) {
            if (sortDirection.equals("asc")) {
                productList.sort(Comparator.comparing(Products::getPrice));
            } else if (sortDirection.equals("desc")) {
                productList.sort(Comparator.comparing(Products::getPrice).reversed());
            }
        }

        return productList;
    }

    @Override
    public List<Products> filterProductsForAdmin(String search, Integer categoryId, Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
        List<Products> productList = new ArrayList<>();

        if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
            productList = productRepository.filterProductsForCus(search, categoryId, minPrice, maxPrice);
        } else {
            productList = productRepository.ViewProductLandingPage();
        }

        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        for (Products product : productList) {
            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
        }

        // Sắp xếp danh sách sản phẩm theo giá
        if (sortDirection != null) {
            if (sortDirection.equals("asc")) {
                productList.sort(Comparator.comparing(Products::getPrice));
            } else if (sortDirection.equals("desc")) {
                productList.sort(Comparator.comparing(Products::getPrice).reversed());
            }
        }

        return productList;
    }



    //này là dành cho trang homepage
    @Override
    public List<Products> GetAllProductForCustomer() {
        List<Products> productList = productRepository.ViewProductLandingPage(); //sản phẩm có status là kích hoạt thì cho hiển thị lên trang home
        if (productList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        for (Products product : productList) {
            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
        }
        return productList;
    }

    //này là giày cho trang quản lí của admin , list hết product ra
    @Override
    public List<Products> GetAllProductForAdmin() {
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
    public Products GetProductById(int product_id) {
        Products products = productRepository.findById(product_id);
        //  String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
        products.setImage(getAddressLocalComputer(products.getImage()));

        return products;
    }


    @Override
    public List<Products> findProductByNameCode(String key) {
        List<Products> productsList = productRepository.findProductByNameCode(key);
        if (productsList.size() == 0) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        for (Products products : productsList) {

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
        if (!checkConditionService.checkInputQuantityInt(productDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(productDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }

    private void validateProductDTO1(ProductAddDTO productAddDTO) {
        if (!checkConditionService.checkInputName(productAddDTO.getProduct_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
//        if (!checkConditionService.checkInputQuantityInt(productDTO1.getQuantity())) {
//            throw new AppException(ErrorCode.QUANTITY_INVALID);
//        }
        if (!checkConditionService.checkInputPrice(productAddDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }

    private void validateProductEditDTO(ProductEditDTO productEditDTO) {
        if (!checkConditionService.checkInputName(productEditDTO.getProduct_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
//        if (!checkConditionService.checkInputQuantityInt(productDTO1.getQuantity())) {
//            throw new AppException(ErrorCode.QUANTITY_INVALID);
//        }
        if (!checkConditionService.checkInputPrice(productEditDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }


    //Đơn xuất vật liệu(Tạo đơn xuất vật liệu cho sản phẩm  UC_30)
    //manger list ra 1 danh sách các sản phẩm có trong đơn hàng , manager chọn vào xuất nguyên vật liệu cho product có trong đơn hàng(list order detail)
    //nếu xuất đơn mà ko đủ quantity thì sẽ hiển thị thông báo lỗi -> người dùng vào nhập thêo sub_material, bấm xuất lại đơn .
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(int productId, Map<Integer, Double> subMaterialQuantities) {
        Products product = productRepository.findById(productId);

        List<ProductSubMaterials> productSubMaterialsList = new ArrayList<>();
        Map<String, String> errors = new HashMap<>(); //hashmap cho error

        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            int subMaterialId = entry.getKey();
            double quantity = entry.getValue();
            SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);

            double currentQuantity = subMaterial.getQuantity();
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

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<List<RequestProductsSubmaterials>>> createExportMaterialProductRequest(int request_product_id, Map<Integer, Double> subMaterialQuantities) {
        RequestProducts requestProducts = requestProductRepository.findById(request_product_id);

        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = new ArrayList<>();
        Map<String, String> errors = new HashMap<>(); //hashmap cho error

        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            int subMaterialId = entry.getKey();
            double quantity = entry.getValue();
            SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);

            double currentQuantity = subMaterial.getQuantity();
            if (quantity > currentQuantity) {
                errors.put(subMaterial.getSubMaterialName(), "Không đủ số lượng");
                continue;
            }

            subMaterial.setQuantity(currentQuantity - quantity);
            subMaterialsRepository.save(subMaterial);

            RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial, requestProducts, quantity);
            requestProductsSubmaterialsList.add(requestProductsSubmaterials);
        }
        ApiResponse<List<RequestProductsSubmaterials>> apiResponse = new ApiResponse<>();
        if (!errors.isEmpty()) {
            apiResponse.setError(1028, errors);
            return ResponseEntity.badRequest().body(apiResponse);
        } else {
            apiResponse.setResult(requestProductsSubmaterialsRepository.saveAll(requestProductsSubmaterialsList));
            return ResponseEntity.ok(apiResponse);
        }
    }
    //Đơn tạo đơn xuất vật liệu cho Employee

}