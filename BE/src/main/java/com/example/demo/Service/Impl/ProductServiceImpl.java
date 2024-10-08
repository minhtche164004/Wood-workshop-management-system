package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestProductEditDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
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
    private SubMaterialsRepository subMaterialsRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private EntityManager entityManager;
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
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Autowired
    private OrderRepository orderRepository;



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

        products.setProductName(productAddDTO.getProduct_name().trim());
        products.setDescription(productAddDTO.getDescription().trim());
        products.setPrice(productAddDTO.getPrice());


        Status_Product status = statusProductRepository.findById(2);//tuc la kich hoạt
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productAddDTO.getCategory_id());

        products.setCategories(categories);
        products.setType(0);


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
                productEditDTO.getProduct_name().trim(),
                productEditDTO.getDescription().trim(),
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
    public RequestProducts EditRequestProduct(int id, RequestProductEditDTO requestProductEditDTO, MultipartFile[] multipartFiles) throws IOException {
        RequestProducts products = requestProductRepository.findById(id);
        if (multipartFiles != null &&
                Arrays.stream(multipartFiles).anyMatch(file -> file != null && !file.isEmpty())) {
            List<Product_Requestimages> productimages= productRequestimagesRepository.findImageByProductId(id);
            for(Product_Requestimages productimages1 : productimages){
                String full_path= productimages1.getFullPath();
                String id_image =cloudinaryService.extractPublicIdFromUrl(full_path);
                cloudinaryService.deleteImage(id_image);
            }
            productRequestimagesRepository.deleteRequestProductImages(id); // Xóa những ảnh trước đó
            uploadImageService.uploadFileRequestProduct(multipartFiles, products.getRequestProductId());
        }
        //ko đc chỉnh sửa quantity
      //  validateProductEditDTO(productEditDTO);
        requestProductRepository.updateRequestProduct(id,
                requestProductEditDTO.getRequestProductName().trim(),
                requestProductEditDTO.getDescription().trim(),
                requestProductEditDTO.getPrice(),
                requestProductEditDTO.getStatus_id(),
                requestProductEditDTO.getQuantity(),
                requestProductEditDTO.getCompletionTime()
        );
        Orders orders = products.getOrders();
        if (orders.getStatus().getStatus_id() == 1) {
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal totalOrder= BigDecimal.ZERO;
            List<Orderdetails> list = orderDetailRepository.getOrderDetailByRequestProductIdAndOrderIdTest(orders.getOrderId());
            List<Orderdetails> orderDetailsList = orderDetailRepository.getOrderDetailByRequestProductIdAndOrderId(products.getRequestProductId(), orders.getOrderId());
            list.removeAll(orderDetailsList);
            for (Orderdetails orderDetail : list) {
                BigDecimal itemPrice = orderDetail.getUnitPrice();
                BigDecimal itemQuantity = BigDecimal.valueOf(orderDetail.getQuantity());
                total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
                totalOrder = totalOrder.add(total); // Cộng dồn total của orderDetail vào totalOrder
            }
            BigDecimal totalCost = BigDecimal.ZERO;
            BigDecimal currentCost = BigDecimal.ZERO;
            BigDecimal totalOrder1= BigDecimal.ZERO;
            // Tính tổng tiền của tất cả các chi tiết đơn hàng
            for (Orderdetails orderDetail : orderDetailsList) {
                currentCost =currentCost.add(requestProductEditDTO.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
                totalOrder1 = totalOrder1.add(currentCost); // Cộng dồn total của orderDetail vào totalOrder
                orderDetail.setUnitPrice(requestProductEditDTO.getPrice());
                orderDetailRepository.save(orderDetail);
            }
            orders.setDeposite((totalOrder.add(totalOrder1)).multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
            orders.setTotalAmount(totalOrder.add(totalOrder1));
            orderRepository.save(orders);
        }

        return products;
    }


    @Override
    public List<Products> findByPriceRange(BigDecimal min, BigDecimal max) {
        List<Products> productsList = productRepository.findByPriceRange(min,max);
        if(productsList == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productsList;
    }

    @Transactional
    @Override
    public Products UpdateStatusProduct(int product_id, int status_id) {
        productRepository.updateStatus(product_id, status_id);
        return productRepository.findById(product_id);
    }

    @Transactional
    @Override
    public void DeleteProduct(int product_id) {
        Products product = productRepository.findById(product_id);
        if(product == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        // Kiểm tra các ràng buộc
       // productImageRepository.findImageByProductId(product_id).isEmpty()
       // productSubMaterialsRepository.findByProductID(product_id).isEmpty()
            if (orderDetailRepository.getOrderDetailByProductId(product_id).isEmpty() &&
                jobRepository.getJobByProductId(product_id).isEmpty() &&
                processproducterrorRepository.getProcessproducterrorByProductId(product_id).isEmpty() &&
                wishListRepository.findByProductID(product_id).isEmpty()
              ) {
            // Không có ràng buộc nào, có thể xóa sản phẩm
                List<Productimages> productImages = productImageRepository.findImageByProductId(product_id);
                for (Productimages productImage : productImages) {
                    productImageRepository.deleteProductimagesById(productImage.getProductImageId());
                }
                List<ProductSubMaterials> productSubMaterialsList = productSubMaterialsRepository.findByProductID(product_id);
                for (ProductSubMaterials productSubMaterials : productSubMaterialsList) {
                    productSubMaterialsRepository.deleteProductSubMaterialsById(productSubMaterials.getProductSubMaterialId());
                }
//            productImageRepository.deleteAll(productImageRepository.findImageByProductId(product_id));
//            productSubMaterialsRepository.deleteAll(productSubMaterialsRepository.findByProductID(product_id));
            productRepository.deleteByProductId(product_id);

        } else {
            // Có ràng buộc, không thể xóa sản phẩm
            throw new AppException(ErrorCode.PRODUCT_HAS_RELATIONSHIPS); // Hoặc một mã lỗi phù hợp
        }
    }




    @Override
    public ProductDetailDTO GetProductByIdWithImage(int id) {
        List<Productimages> productimagesList = productImageRepository.findImageByProductId(id);
        Products products = productRepository.findById(id);
        if (products == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        ProductDetailDTO productDTOShow = new ProductDetailDTO();
        if (productimagesList == null) {
            productDTOShow.setImageList(null);
        }
        productDTOShow.setProductId(products.getProductId());
        productDTOShow.setQuantity(products.getQuantity());
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
        List<SubMaterialViewDTO> list = productSubMaterialsRepository.GetSubMaterialByProductId(id);
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
//    public List<Products> filterProductForCustomer(String search, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
//        List<Products> productList = new ArrayList<>();
//
//        if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
//            productList = productRepository.filterProductsForCus(search, categoryId, minPrice, maxPrice);
//        } else {
//            productList = productRepository.ViewProductLandingPage();
//        }
//
//        if (productList.isEmpty()) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//
//        for (Products product : productList) {
//            product.setImage(getAddressLocalComputer(product.getImage())); // Cập nhật lại đường dẫn ảnh
//        }
//
//        // Sắp xếp danh sách sản phẩm theo giá
//        if (sortDirection != null) {
//            if (sortDirection.equals("asc")) {
//                productList.sort(Comparator.comparing(Products::getPrice));
//            } else if (sortDirection.equals("desc")) {
//                productList.sort(Comparator.comparing(Products::getPrice).reversed());
//            }
//        }
//
//        return productList;
//    }
    public List<Products> filterProductForCustomer(String search, Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection) {
    List<Products> productList = new ArrayList<>();

    if (search != null || categoryId != null || minPrice != null || maxPrice != null) {
        String searchTerm = search == null ? "" : search.trim();
        productList = productRepository.filterProductsForCus(searchTerm, categoryId, minPrice, maxPrice);
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

        if (search != null || categoryId != null || statusId != null || minPrice != null || maxPrice != null) {
            String searchTerm = search == null ? "" : search.trim();
            productList = productRepository.filterProductsForAdmin(searchTerm, categoryId, statusId, minPrice, maxPrice);
        } else {
            productList = productRepository.findAll();
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
        List<Products> productsList = productRepository.findProductByNameCode(key.trim());
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
        if (!checkConditionService.checkInputName(productDTO.getProduct_name().trim())) {
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
//        if (!checkConditionService.checkInputName(productAddDTO.getProduct_name())) {
//            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
//        }
//        if (!checkConditionService.checkInputQuantityInt(productDTO1.getQuantity())) {
//            throw new AppException(ErrorCode.QUANTITY_INVALID);
//        }
        if (!checkConditionService.checkInputPrice(productAddDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }

    private void validateProductEditDTO(ProductEditDTO productEditDTO) {
//        if (!checkConditionService.checkInputName(productEditDTO.getProduct_name())) {
//            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
//        }
//        if (!checkConditionService.checkInputQuantityInt(productDTO1.getQuantity())) {
//            throw new AppException(ErrorCode.QUANTITY_INVALID);
//        }
        if (!checkConditionService.checkInputPrice(productEditDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
    }

    @Override
    public List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId) {
        List<SubMateProductDTO> list = productSubMaterialsRepository.getProductSubMaterialByProductIdDTO(productId);
        if(list == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<SubMateProductRequestDTO> getRequestProductSubMaterialByRequestProductIdDTO(int re_productId) {
        List<SubMateProductRequestDTO> list = requestProductsSubmaterialsRepository.getRequestProductSubMaterialByRequestProductIdDTO(re_productId);
        if(list == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }



    @Transactional
    @Override
    public void DeleteRequestProduct(int re_product_id) {
        RequestProducts product = requestProductRepository.findById(re_product_id);
        if(product == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        // Kiểm tra các ràng buộc
        // productImageRepository.findImageByProductId(product_id).isEmpty()
        // productSubMaterialsRepository.findByProductID(product_id).isEmpty()
        if (
                jobRepository.getJobByRequestProductIdCheck(re_product_id).isEmpty() &&
                processproducterrorRepository.getProcessproducterrorByRequestProductId(re_product_id).isEmpty()
        ) {
            List<Jobs> list_job = jobRepository.getJobByRequestProductId(re_product_id);
            for(Jobs job : list_job){
                jobRepository.deleteJobById(job.getJobId());
            }
            List<Orderdetails> list = orderDetailRepository.getOrderDetailByRequestProductId(re_product_id);
            for (Orderdetails o : list) {
                orderDetailRepository.deleteOrderDetailByOrderDetailId(o.getOrderDetailId());
            }
            // Không có ràng buộc nào, có thể xóa sản phẩm
            List<Product_Requestimages> productImages = productRequestimagesRepository.findImageByProductId(re_product_id);
            for (Product_Requestimages productImage : productImages) {
                productRequestimagesRepository.deleteRequestProductimagesById(productImage.getProductImageId());
            }
            List<RequestProductsSubmaterials> productSubMaterialsList = requestProductsSubmaterialsRepository.findByRequestProductID(re_product_id);
            for (RequestProductsSubmaterials productSubMaterials : productSubMaterialsList) {
                requestProductsSubmaterialsRepository.deleteRequestProductSubMaterialsById(productSubMaterials.getRequestProductsSubmaterialsId());
            }
//            productImageRepository.deleteAll(productImageRepository.findImageByProductId(product_id));
//            productSubMaterialsRepository.deleteAll(productSubMaterialsRepository.findByProductID(product_id));
            requestProductRepository.deleteByRequestProductId(re_product_id);
            BigDecimal total = BigDecimal.ZERO;
            Orders order = product.getOrders();
            List<Orderdetails> list_new = orderDetailRepository.getOrderDetailByOrderId(order.getOrderId());
            for(Orderdetails o :list_new){
                BigDecimal itemPrice = o.getUnitPrice();
                BigDecimal itemQuantity = BigDecimal.valueOf(o.getQuantity());
                total = total.add(itemPrice.multiply(itemQuantity)); // Cộng dồn vào total
            }
            order.setDeposite(total.multiply(BigDecimal.valueOf(0.2))); // 20% tiền cọc của tổng tiền đơn hàng
            order.setTotalAmount(total);
            orderRepository.save(order);

        } else {
            // Có ràng buộc, không thể xóa sản phẩm
            throw new AppException(ErrorCode.PRODUCT_HAS_RELATIONSHIPS); // Hoặc một mã lỗi phù hợp
        }
    }



}