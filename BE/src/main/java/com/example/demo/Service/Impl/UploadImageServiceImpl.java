package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Entity.Productimages;
import com.example.demo.Entity.Products;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.UploadImageService;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
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
    private CheckConditionService checkConditionService;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ModelMapper modelMapper;
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

//                Files.write(Paths.get(uploadPath + fileNameUpload), bytes);
                String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
                String absoluteUploadPath = projectDir + uploadPath;
                Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
                Files.write(filePath, bytes);
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


    // Hàm uploadFile ảnh thumbnail, trả về Product_Thumbnail VỚI file_path
    @Override
    public Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFile) { // Nhận một MultipartFile duy nhất
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
        try {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String fileExtension = getFileExtension(filename);

            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                throw new AppException(ErrorCode.IMAGE_INVALID);
            }
            byte[] bytes = multipartFile.getBytes();

            // Tạo tên tệp mới (có thể thêm logic tạo thumbnail ở đây)
            String fileNameUpload = "thumbnail_" + FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;

            // Ghi tệp vào thư mục upload
//            Path filePath = Paths.get(uploadPath + fileNameUpload);
//            Files.write(filePath, bytes);
            String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
            String absoluteUploadPath = projectDir + uploadPath;
            Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
            Files.write(filePath, bytes);
            // Tạo và trả về đối tượng Product_Thumbnail
            Product_Thumbnail thumbnail = new Product_Thumbnail();
            thumbnail.setFullPath(filePath.toString()); // Đường dẫn đầy đủ của ảnh thumbnail
            return thumbnail;

        } catch (IOException ex) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
    }


    // Hàm lấy phần mở rộng của tệp tin
    public String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        return filename.substring(dotIndex + 1);
    }}
