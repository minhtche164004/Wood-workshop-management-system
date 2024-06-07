package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.StatusRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


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
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Products AddNewProduct(ProductDTO productDTO) {
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
        productRepository.save(products);

        // Lưu sản phẩm trước để có productId
        products = productRepository.save(products);

        return products;
    }

    @Value("${upload.file.path}")
    private String uploadPath;
    @Value("${upload.file.extension}")
    private String fileExtension;

    // Hàm uploadFile nhận vào một mảng các tệp tin (MultipartFile[]) và ID của sản phẩm
    @Override
    public List<ProductImageDTO> uploadFile(MultipartFile[] multipartFiles, int product_id) {

        // Tìm sản phẩm tương ứng với product_id
        Products products = productRepository.findById(product_id);
        // Nếu không tìm thấy sản phẩm hoặc không có tệp tin nào được chọn, ném ra ngoại lệ
        if (multipartFiles == null) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }

        // Tạo danh sách để lưu thông tin ảnh sẽ được upload
        List<Productimages> fileUploads = new ArrayList<>();

        // Duyệt qua từng tệp tin trong mảng multipartFiles
        Arrays.stream(multipartFiles).forEach(file -> {
            try {
                // Lấy tên tệp gốc và loại bỏ các ký tự không an toàn
                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                // Lấy phần mở rộng của tệp tin
                String fileExtension = getFileExtension(filename);

                // Kiểm tra xem phần mở rộng tệp tin có hợp lệ không
                List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png"); // Danh sách các phần mở rộng hợp lệ
                if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                    throw new AppException(ErrorCode.IMAGE_INVALID);
                }

                // Đọc nội dung tệp tin thành mảng byte
                byte[] bytes = file.getBytes();

                // Tạo tên tệp mới để tránh trùng lặp
                var fileNameUpload = FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;

                // Ghi tệp tin vào thư mục upload với tên tệp mới
                Files.write(Paths.get(uploadPath + fileNameUpload), bytes);

                // Tạo đối tượng Productimages để lưu thông tin ảnh vào cơ sở dữ liệu
                Productimages fileUpload = new Productimages();
                fileUpload.setImage_name(fileNameUpload); // Tên tệp mới
                fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename)); // Tên tệp gốc (không có phần mở rộng)
                fileUpload.setExtension_name(fileExtension); // Phần mở rộng tệp tin
                fileUpload.setFullPath(uploadPath + fileNameUpload); // Đường dẫn đầy đủ đến tệp tin đã upload
                fileUpload.setProduct(products); // Liên kết ảnh với sản phẩm

                // Thêm đối tượng Productimages vào danh sách
                fileUploads.add(fileUpload);
            } catch (IOException ex) { // Xử lý ngoại lệ nếu có lỗi xảy ra trong quá trình đọc/ghi tệp tin
                throw new AppException(ErrorCode.IMAGE_INVALID);
            }
        });

        // Lưu tất cả các đối tượng Productimages vào cơ sở dữ liệu
        productImageRepository.saveAll(fileUploads);

        // Chuyển đổi danh sách Productimages thành danh sách ProductImageDTO
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Productimages image : fileUploads) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId());
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath());
            dto.setFileOriginalName(image.getFileOriginalName());
            dto.setProduct_id(image.getProduct().getProductId());
            productImageDTOs.add(dto);
        }

        // Trả về danh sách ProductImageDTO
        return productImageDTOs;
    }

    // Hàm lấy phần mở rộng của tệp tin
    public String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        return filename.substring(dotIndex + 1);
    }
}