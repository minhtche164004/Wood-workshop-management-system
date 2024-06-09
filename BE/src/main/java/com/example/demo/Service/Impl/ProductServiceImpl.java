package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO_Show;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
    private ProductImageRepository productImageRepository;
    @Autowired
    private UploadImageService uploadImageService;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Products AddNewProduct(ProductDTO productDTO,MultipartFile[] multipartFiles,MultipartFile multipartFiles_thumbnal) {
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
        //    products.setImage(productDTO.getImage());
        products.setQuantity(productDTO.getQuantity());

        if (!checkConditionService.checkInputName(productDTO.getProduct_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (productRepository.countByProductName(productDTO.getProduct_name()) > 0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        if (!checkConditionService.checkInputQuantity(productDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(productDTO.getPrice())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }
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
        products=productRepository.save(products);
        //set ảnh của product
        Products product =productRepository.findByName(productDTO.getProduct_name());
        uploadImageService.uploadFile(multipartFiles, product.getProductId());
        return products;
    }


    @Override
    public List<ProductDTO_Show> GetAllProduct(){
        List<Products> product_list = productRepository.findAll();
        if (product_list.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return product_list.stream()
                .map(product -> modelMapper.map(product, ProductDTO_Show.class))
                .collect(Collectors.toList());
    }
}