package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.StatusRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
@Autowired
private StatusRepository statusRepository;
@Autowired
private CategoryRepository categoryRepository;
@Autowired
private ProductRepository productRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private ProductImageRepository productImageRepository;

    @Override
    public Products AddNewProduct(ProductDTO productDTO){
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

        Status status = statusRepository.findById(productDTO.getStatus_id());
        products.setStatus(status);
        Categories categories = categoryRepository.findById(productDTO.getCategory_id());
        products.setCategories(categories);
        products.setType(productDTO.getType());
    //    products.setImage(productDTO.getImage());
        products.setQuantity(productDTO.getQuantity());

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        Products lastProduct = productRepository.findProductTop(dateString + "PD");
        int count = lastProduct != null ? Integer.parseInt(lastProduct.getCode().substring(8)) + 1 : 1;
        String code = dateString + "PD" + String.format("%03d", count);
        products.setCode(code);
        productRepository.save(products);

        // Lưu sản phẩm trước để có productId
        products = productRepository.save(products);

        return products;
    }
////upload theo từng cái một
//@Override
//    public String uploadImage(MultipartFile file, int id) throws IOException {
//        Products product = productRepository.findById(id);
//     Productimages productimages =productImageRepository.save(Productimages.builder()
//                        .image_name(file.getOriginalFilename())
//                                .type(file.getContentType())
//                     .product(product)
//                                        .imageData(ImageUtils.compressImage(file.getBytes())).
//                build());
//        if(productimages!=null){
//            return "file uploaded success" +file.getOriginalFilename();
//        }
//        return null;
//    }

//Upload ảnh theo 1 list nhiều cái
    @Override
    public List<String> uploadImagesList(List<MultipartFile> files, int id) throws IOException {
        Products product = productRepository.findById(id); // Lấy thông tin sản phẩm
        List<String> uploadedImagePaths = new ArrayList<>();
        for (MultipartFile file : files) {
            // Kiểm tra xem file có rỗng không
            if (file.isEmpty()) {
                continue; // Bỏ qua file rỗng
            }

            // Tạo đối tượng ProductImages
            Productimages productImage = Productimages.builder()
                    .image_name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .product(product)
                    .imageData(ImageUtils.compressImage(file.getBytes()))
                    .build();
            productImageRepository.save(productImage);

            // Lưu đường dẫn ảnh (nếu cần)
            uploadedImagePaths.add(file.getOriginalFilename());
        }

        return uploadedImagePaths; // Trả về danh sách đường dẫn ảnh
    }


    @Override
    //load hình ảnh
    public byte[] dowloadImage(String fileName){
        Optional<Productimages> dbproductimages=productImageRepository.findByImage_name(fileName);
       byte[] images = ImageUtils.decompressImage(dbproductimages.get().getImageData());
       return images;
    }


//load 1 list image
@Override
public List<byte[]> downloadImagesByProductList(int productId) {
    Products products=productRepository.findById(productId);
    List<Productimages> productImages = productImageRepository.findByImage_Id(products.getProductId());
    return productImages.stream()
            .map(Productimages::getImageData)
            .map(ImageUtils::decompressImage) // Giải nén từng ảnh
            .collect(Collectors.toList()); // Trả về danh sách ảnh đã giải nén
}

}


