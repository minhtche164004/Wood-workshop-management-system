package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO;
import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.Status;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.StatusRepository;
import com.example.demo.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
@Autowired
private StatusRepository statusRepository;
@Autowired
private CategoryRepository categoryRepository;
@Autowired
private ProductRepository productRepository;

    @Override
    public Products AddNewProduct(ProductDTO productDTO) {
        Products products = new Products();
        // Chuyển đổi completion_time sang java.sql.Date
        LocalDate currentDate = LocalDate.now();
        java.sql.Date sqlCompletionTime = java.sql.Date.valueOf(currentDate); // Chuyển đổi sang java.sql.Date
        products.setCompletionTime(sqlCompletionTime);

        // Tính toán EndDateWarranty (3 năm sau thời điểm hiện tại)
        LocalDate endDateWarranty = currentDate.plusYears(3);
        java.sql.Date sqlEndDateWarranty = java.sql.Date.valueOf(endDateWarranty);
        products.setEnddateWarranty(sqlEndDateWarranty);

        products.setProductName(productDTO.getProduct_name());
        products.setDescription(productDTO.getProduct_name());
        products.setPrice(productDTO.getPrice());

        Status status = statusRepository.findById(productDTO.getStatus_id());
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productDTO.getCaregoty_id());
        products.setCategories(categories);
        products.setType(productDTO.getType());
        products.setImage(productDTO.getImage());
        products.setQuantity(productDTO.getQuantity());


        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")); // Định dạng ngày tháng
        int orderNumber = productRepository.countByCreatedAtDate(currentDate) + 1; // Đếm số lượng sản phẩm đã tạo trong ngày + 1
        String code = formattedDate + "PD" + String.format("%02d", orderNumber); // Định dạng mã sản phẩm
        products.setCode(code);


        productRepository.save(products);
        return products;
    }
}
