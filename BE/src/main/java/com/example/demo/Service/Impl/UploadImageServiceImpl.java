package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.UploadImageService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class UploadImageServiceImpl implements UploadImageService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Autowired
    private RequestimagesRepository requestimagesRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Value("${upload.file.path}")
    private String uploadPath;
    @Value("${upload.file.extension}")
    private String fileExtension;
    private static final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class); // Sử dụng SLF4J Logger


    @Override
    public Product_Thumbnail uploadFile_Thumnail(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }

        String fileExtension = getFileExtension(imageName);

        // Kiểm tra định dạng tệp - xử lý khi fileExtension null
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
        if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }

        // Tạo tên tệp mới (có thể thêm logic tạo thumbnail ở đây nếu cần)
        String fileNameUpload = "thumbnail_" + FilenameUtils.removeExtension(imageName) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;
        // Thêm uploadPath vào tên file
        String fullPath = uploadPath  + fileNameUpload; // Thêm dấu "/" nếu cần
        // Không cần phần lưu trữ ảnh nữa

        // Tạo và trả về đối tượng Product_Thumbnail, chỉ chứa tên file
        Product_Thumbnail thumbnail = new Product_Thumbnail();
        thumbnail.setFullPath(fullPath);
        return thumbnail;
    }


    @Override
    public List<ProductImageDTO> uploadFile(List<String> imageNames, int product_id) {
        Products product = productRepository.findById(product_id);

        if (product == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        List<Productimages> productImages = new ArrayList<>();

        // Duyệt qua danh sách tên ảnh
        for (String imageName : imageNames) {
            // Kiểm tra định dạng tệp (bạn nên làm điều này ở frontend)
            String fileExtension = getFileExtension(imageName);
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                throw new AppException(ErrorCode.IMAGE_INVALID);
            }

            // Tạo đối tượng Productimages từ tên ảnh và thêm đường dẫn lưu trữ
            Productimages productImage = new Productimages();
            productImage.setImage_name(imageName);
            productImage.setFullPath(uploadPath + imageName); // Lưu đường dẫn đầy đủ
            productImage.setProduct(product);
            productImages.add(productImage);
        }

        productImageRepository.saveAll(productImages);

        // Chuyển đổi danh sách Productimages thành danh sách ProductImageDTO
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Productimages image : productImages) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId());
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath()); // Đường dẫn đầy đủ
            dto.setProduct_id(image.getProduct().getProductId());
            productImageDTOs.add(dto);
        }
        return productImageDTOs;
    }

    @Override
    public List<ProductImageDTO> uploadFile_Request(List<String> imageNames, int requestId) {
        Requests request = requestRepository.findById(requestId);

        if (request == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        List<Requestimages> requestImages = new ArrayList<>();

        // Duyệt qua danh sách tên ảnh
        for (String imageName : imageNames) {
            // Kiểm tra định dạng tệp (bạn nên làm điều này ở frontend)
            String fileExtension = getFileExtension(imageName);
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                throw new AppException(ErrorCode.IMAGE_INVALID);
            }

            // Tạo đối tượng Requestimages từ tên ảnh và thêm đường dẫn lưu trữ
            Requestimages requestImage = new Requestimages();
            requestImage.setImage_name(imageName);
            requestImage.setFullPath(uploadPath  + imageName); // Lưu đường dẫn đầy đủ
            requestImage.setRequests(request);
            requestImages.add(requestImage);
        }

        requestimagesRepository.saveAll(requestImages);

        // Chuyển đổi danh sách Requestimages thành danh sách ProductImageDTO
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Requestimages image : requestImages) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId()); // Giả sử Requestimages cũng có trường productImageId
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath());
            dto.setProduct_id(image.getRequests().getRequestId());
            productImageDTOs.add(dto);
        }

        return productImageDTOs;
    }

    @Override
    public List<ProductImageDTO> uploadFile_RequestProduct(List<String> imageNames, int requestProductId) {
        RequestProducts requestProducts = requestProductRepository.findById(requestProductId);

        if (requestProducts == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        List<Product_Requestimages> productRequestImages = new ArrayList<>(); // Chú ý: Sử dụng đúng entity Product_Requestimages

        for (String imageName : imageNames) {
            String fileExtension = getFileExtension(imageName);
            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
                throw new AppException(ErrorCode.IMAGE_INVALID);
            }

            Product_Requestimages productRequestImage = new Product_Requestimages(); // Chú ý: Sử dụng đúng entity Product_Requestimages
            productRequestImage.setImage_name(imageName);
            productRequestImage.setFullPath(uploadPath  + imageName); // Lưu đường dẫn đầy đủ
            productRequestImage.setRequestProducts(requestProducts);
            productRequestImages.add(productRequestImage);
        }

        productRequestimagesRepository.saveAll(productRequestImages); // Sử dụng repository phù hợp với Product_Requestimages

        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Product_Requestimages image : productRequestImages) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId());
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath());
            dto.setProduct_id(image.getRequestProducts().getRequestProductId()); // Lấy ID của RequestProducts
            productImageDTOs.add(dto);
        }

        return productImageDTOs;
    }


    // Hàm lấy phần mở rộng của tệp tin
    public String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        return filename.substring(dotIndex + 1);
    }}
