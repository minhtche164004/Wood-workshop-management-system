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


//    @Override
//    public Product_Thumbnail uploadFile_Thumnail(String imageName) {
//        if (imageName == null || imageName.isEmpty()) {
//            throw new AppException(ErrorCode.IMAGE_INVALID);
//        }
//
//        String fileExtension = getFileExtension(imageName);
//
//        // Kiểm tra định dạng tệp - xử lý khi fileExtension null
//        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
//        if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
//            throw new AppException(ErrorCode.IMAGE_INVALID);
//        }
//
//        // Tạo tên tệp mới (có thể thêm logic tạo thumbnail ở đây nếu cần)
//        String fileNameUpload = "thumbnail_" + FilenameUtils.removeExtension(imageName) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;
//        // Thêm uploadPath vào tên file
//        String fullPath = uploadPath  + fileNameUpload; // Thêm dấu "/" nếu cần
//        // Không cần phần lưu trữ ảnh nữa
//
//        // Tạo và trả về đối tượng Product_Thumbnail, chỉ chứa tên file
//        Product_Thumbnail thumbnail = new Product_Thumbnail();
//        thumbnail.setFullPath(fullPath);
//        return thumbnail;
//    }
//
//
//    @Override
//    public List<ProductImageDTO> uploadFile(List<String> imageNames, int product_id) {
//        Products product = productRepository.findById(product_id);
//
//        if (product == null) {
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//
//        List<Productimages> productImages = new ArrayList<>();
//
//        // Duyệt qua danh sách tên ảnh
//        for (String imageName : imageNames) {
//            // Kiểm tra định dạng tệp (bạn nên làm điều này ở frontend)
//            String fileExtension = getFileExtension(imageName);
//            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
//            if (fileExtension == null || !allowedExtensions.contains(fileExtension.toLowerCase())) {
//                throw new AppException(ErrorCode.IMAGE_INVALID);
//            }
//
//            // Tạo đối tượng Productimages từ tên ảnh và thêm đường dẫn lưu trữ
//            Productimages productImage = new Productimages();
//            productImage.setImage_name(imageName);
//            productImage.setFullPath(uploadPath + imageName); // Lưu đường dẫn đầy đủ
//            productImage.setProduct(product);
//            productImages.add(productImage);
//        }
//
//        productImageRepository.saveAll(productImages);
//
//        // Chuyển đổi danh sách Productimages thành danh sách ProductImageDTO
//        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
//        for (Productimages image : productImages) {
//            ProductImageDTO dto = new ProductImageDTO();
//            dto.setProductImageId(image.getProductImageId());
//            dto.setImage_name(image.getImage_name());
//            dto.setFullPath(image.getFullPath()); // Đường dẫn đầy đủ
//            dto.setProduct_id(image.getProduct().getProductId());
//            productImageDTOs.add(dto);
//        }
//        return productImageDTOs;
//    }
//up cho request product
@Override
public List<ProductImageDTO> uploadFileRequestProduct(MultipartFile[] multipartFiles, int requestProduct_id) {

    // Tìm sản phẩm tương ứng với requestProduct_id
    RequestProducts requestProducts = requestProductRepository.findById(requestProduct_id);

        // Nếu không tìm thấy sản phẩm, ném ra ngoại lệ
        if (requestProducts == null) {

                throw new AppException(ErrorCode.NOT_FOUND);
            }

            // Tạo danh sách để lưu thông tin ảnh sẽ được upload
            List<Product_Requestimages> fileUploads = new ArrayList<>();

            if (multipartFiles != null && multipartFiles.length > 0) {
                // Duyệt qua từng tệp tin trong mảng multipartFiles
                for (MultipartFile file : multipartFiles) {
                    if (file != null && !file.isEmpty()) { // Kiểm tra file != null và không rỗng
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

                            String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
                            Path projectDirPath = Paths.get(projectDir);
                            Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
                            String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
                            String absoluteUploadPath = desiredPath + uploadPath;
                            Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
                            Files.write(filePath, bytes);
                            // Tạo đối tượng Productimages để lưu thông tin ảnh vào cơ sở dữ liệu
                            Product_Requestimages fileUpload = new Product_Requestimages();
                            fileUpload.setImage_name(fileNameUpload); // Tên tệp mới
                            fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename)); // Tên tệp gốc (không có phần mở rộng)
                            fileUpload.setExtension_name(fileExtension); // Phần mở rộng tệp tin
                            fileUpload.setFullPath(uploadPath + fileNameUpload); // Đường dẫn đầy đủ đến tệp tin đã upload
                            fileUpload.setRequestProducts(requestProducts); // Liên kết ảnh với sản phẩm

                            // Thêm đối tượng Productimages vào danh sách
                            fileUploads.add(fileUpload);
                        } catch (IOException ex) { // Xử lý ngoại lệ nếu có lỗi xảy ra trong quá trình đọc/ghi tệp tin
                            throw new AppException(ErrorCode.IMAGE_INVALID);
                        }
                    } else {
                        // Xử lý khi file là null hoặc rỗng (ví dụ: bỏ qua file này)
                        logger.warn("Skipping null or empty file in multipartFiles");
                        return new ArrayList<>(); // Trả về danh sách rỗng nếu không có file
                    }
                }


                // Lưu tất cả các đối tượng Productimages vào cơ sở dữ liệu
                productRequestimagesRepository.saveAll(fileUploads);

                // Chuyển đổi danh sách Productimages thành danh sách ProductImageDTO
                List<ProductImageDTO> productImageDTOs = new ArrayList<>();
                for (Product_Requestimages image : fileUploads) {
                    ProductImageDTO dto = new ProductImageDTO();
                    dto.setProductImageId(image.getProductImageId());
                    dto.setImage_name(image.getImage_name());
                    dto.setFullPath(image.getFullPath());
                    dto.setFileOriginalName(image.getFileOriginalName());
                    dto.setProduct_id(image.getRequestProducts().getRequestProductId());
                    productImageDTOs.add(dto);

                        }

                        // Trả về danh sách ProductImageDTO
                        return productImageDTOs;
                    } else {
                        return new ArrayList<>(); // Trả về danh sách rỗng nếu không có file
                    }

                }
    //up image cua request
    //up cho request product
    @Override
    public List<ProductImageDTO> uploadFileRequest(MultipartFile[] multipartFiles, int request_id) {
        // Tìm request tương ứng với request_id
        Requests request = requestRepository.findById(request_id);


            // Nếu không tìm thấy request, ném ra ngoại lệ
            if (request == null) {
                throw new AppException(ErrorCode.NOT_FOUND);
            }

            List<Requestimages> fileUploads = new ArrayList<>();

            if (multipartFiles != null) { // Kiểm tra multipartFiles != null
                for (MultipartFile file : multipartFiles) {
                    if (file != null && !file.isEmpty()) { // Kiểm tra file != null và không rỗng
                        try {
                            String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                            String fileExtension = getFileExtension(filename);

                            if (fileExtension != null && !fileExtension.isEmpty()) {
                                // Kiểm tra xem phần mở rộng tệp tin có hợp lệ không
                                List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
                                if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                                    throw new AppException(ErrorCode.IMAGE_INVALID);
                                }

                                // Đọc nội dung tệp tin thành mảng byte
                                byte[] bytes = file.getBytes();

                                // Tạo tên tệp mới để tránh trùng lặp
                                var fileNameUpload = FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;

                                // Ghi tệp tin vào thư mục upload với tên tệp mới
                                String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
                                Path projectDirPath = Paths.get(projectDir);
                                Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
                                String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
                                String absoluteUploadPath = desiredPath + uploadPath;
                                Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
                                Files.write(filePath, bytes);

                                // Tạo đối tượng Requestimages để lưu thông tin ảnh vào cơ sở dữ liệu
                                Requestimages fileUpload = new Requestimages();
                                fileUpload.setImage_name(fileNameUpload);
                                fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename));
                                fileUpload.setExtension_name(fileExtension);
                                fileUpload.setFullPath(uploadPath + fileNameUpload);
                                fileUpload.setRequests(request); // Liên kết ảnh với request

                                // Thêm đối tượng Requestimages vào danh sách
                                fileUploads.add(fileUpload);
                            } else {
                                throw new AppException(ErrorCode.IMAGE_INVALID);
                            }
                        } catch (IOException ex) {
                            throw new AppException(ErrorCode.IMAGE_INVALID);
                        }
                    } else {
                        logger.warn("Skipping null or empty file in multipartFiles");
                        // Không return ở đây nữa
                    }
                } // Kết thúc vòng lặp for

                }

                // Lưu tất cả các đối tượng Requestimages vào cơ sở dữ liệu
                requestimagesRepository.saveAll(fileUploads);


                // Chuyển đổi danh sách Requestimages thành danh sách ProductImageDTO
                List<ProductImageDTO> productImageDTOs = new ArrayList<>();
                for (Requestimages image : fileUploads) {

                        ProductImageDTO dto = new ProductImageDTO();
                        dto.setProductImageId(image.getProductImageId());

                        dto.setImage_name(image.getImage_name());
                        dto.setFullPath(image.getFullPath());
                        dto.setFileOriginalName(image.getFileOriginalName());
                        dto.setProduct_id(image.getRequests().getRequestId());
                        productImageDTOs.add(dto);
                    }

                    return productImageDTOs; // Đưa return xuống cuối phương thức

                }

                @Override
    public List<ProductImageDTO> uploadFile(MultipartFile[] multipartFiles, int product_id) {
        // Tìm sản phẩm tương ứng với product_id
        Products products = productRepository.findById(product_id);


        // Nếu không tìm thấy sản phẩm, ném ra ngoại lệ
        if (products == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

            // Tạo danh sách để lưu thông tin ảnh sẽ được upload
            List<Productimages> fileUploads = new ArrayList<>();

            if (multipartFiles != null && multipartFiles.length > 0) {
                // Duyệt qua từng tệp tin trong mảng multipartFiles
                for (MultipartFile file : multipartFiles) {
                    if (file != null && !file.isEmpty()) { // Kiểm tra file != null và không rỗng
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

                            String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
                            Path projectDirPath = Paths.get(projectDir);
                            Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
                            String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
                            String absoluteUploadPath = desiredPath + uploadPath;
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
                    } else {
                        // Xử lý khi file là null hoặc rỗng (ví dụ: bỏ qua file này)
                        logger.warn("Skipping null or empty file in multipartFiles");
                        return new ArrayList<>(); // Trả về danh sách rỗng nếu không có file
                    }
                }

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
            } else {
                return new ArrayList<>(); // Trả về danh sách rỗng nếu không có file
            }
        }
    // Hàm uploadFile ảnh thumbnail, trả về Product_Thumbnail VỚI file_path
    @Override
    public Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFile) { // Nhận một MultipartFile duy nhất
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new AppException(ErrorCode.IMAGE_INVALID);

        }
        try {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            String fileExtension = getFileExtension(filename); //tên file ảnh


            List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
            if (!allowedExtensions.contains(fileExtension.toLowerCase())) {

                throw new AppException(ErrorCode.IMAGE_INVALID);
            }
            byte[] bytes = multipartFile.getBytes();

            // Tạo tên tệp mới (có thể thêm logic tạo thumbnail ở đây)
            String fileNameUpload = "thumbnail_" + FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;

            String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
            Path projectDirPath = Paths.get(projectDir);
            Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
            String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
            String absoluteUploadPath = desiredPath + uploadPath;
            Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
            Files.write(filePath, bytes);
            // Tạo và trả về đối tượng Product_Thumbnail
            Product_Thumbnail thumbnail = new Product_Thumbnail();
            thumbnail.setFullPath(uploadPath + fileNameUpload); // Đường dẫn đầy đủ đến tệp tin đã upload
//            thumbnail.setFullPath(filePath.toString()); // Đường dẫn đầy đủ của ảnh thumbnail
            return thumbnail;

        } catch (IOException ex) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
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
