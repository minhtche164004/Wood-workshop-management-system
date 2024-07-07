package com.example.demo.Service.Impl;

import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.CloudinaryService;
import com.example.demo.Service.UploadImageService;
import jakarta.transaction.Transactional;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    CloudinaryService cloudinaryService;


//    @Value("${upload.file.path}")
//    private String uploadPath;
//    @Value("${upload.file.extension}")
//    private String fileExtension;
    private static final Logger logger = LoggerFactory.getLogger(UploadImageServiceImpl.class); // Sử dụng SLF4J Logger


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
                            // Tải ảnh lên Cloudinary
                            Map result = cloudinaryService.upload(file, "product_images");
                            String cloudinaryUrl = (String) result.get("secure_url"); // Lấy URL từ kết quả trả về

//                            // Đọc nội dung tệp tin thành mảng byte
//                            byte[] bytes = file.getBytes();
//
//                            // Tạo tên tệp mới để tránh trùng lặp
//                            var fileNameUpload = FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;
//
//                            // Ghi tệp tin vào thư mục upload với tên tệp mới
//
//                            String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
//                            Path projectDirPath = Paths.get(projectDir);
//                            Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
//                            String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
//                            String absoluteUploadPath = desiredPath + uploadPath;
//                            Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
//                            Files.write(filePath, bytes);
                            // Tạo đối tượng Productimages để lưu thông tin ảnh vào cơ sở dữ liệu
                            Product_Requestimages fileUpload = new Product_Requestimages();
                            fileUpload.setImage_name(filename); // Tên tệp mới
                            fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename)); // Tên tệp gốc (không có phần mở rộng)
                            fileUpload.setExtension_name(fileExtension); // Phần mở rộng tệp tin
                            fileUpload.setFullPath(cloudinaryUrl); // Đường dẫn đầy đủ đến tệp tin đã upload
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

                                // Tải ảnh lên Cloudinary
                                Map result = cloudinaryService.upload(file, "product_images");
                                String cloudinaryUrl = (String) result.get("secure_url"); // Lấy URL từ kết quả trả về
//                                // Đọc nội dung tệp tin thành mảng byte
//                                byte[] bytes = file.getBytes();
//
//                                // Tạo tên tệp mới để tránh trùng lặp
//                                var fileNameUpload = FilenameUtils.removeExtension(filename) + "_" + Calendar.getInstance().getTimeInMillis() + "." + fileExtension;
//
//                                // Ghi tệp tin vào thư mục upload với tên tệp mới
//                                String projectDir = Paths.get("").toAbsolutePath().toString().replace("\\", "/");
//                                Path projectDirPath = Paths.get(projectDir);
//                                Path parentDir = projectDirPath.getParent(); // Lấy thư mục cha
//                                String desiredPath = parentDir.toString(); // Chuyển đổi thành chuỗi
//                                String absoluteUploadPath = desiredPath + uploadPath;
//                                Path filePath = Paths.get(absoluteUploadPath, fileNameUpload);
//                                Files.write(filePath, bytes);

                                // Tạo đối tượng Requestimages để lưu thông tin ảnh vào cơ sở dữ liệu
                                Requestimages fileUpload = new Requestimages();
                                fileUpload.setImage_name(filename);
                                fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename));
                                fileUpload.setExtension_name(fileExtension);
                                fileUpload.setFullPath(cloudinaryUrl);
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
//        int countFile = multipartFiles.length;
//        int i = 1;
//        HashMap<Integer,String> codeCount = generateMultipleCode(countFile);
        // Nếu không tìm thấy sản phẩm, ném ra ngoại lệ
        if (products == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        // Tạo danh sách để lưu trữ thông tin ảnh sẽ được tải lên
        List<Productimages> fileUploads = new ArrayList<>();
        List<ProductImageDTO> productImageDTOs = new ArrayList<>(); // Tạo danh sách ProductImageDTO

        // Kiểm tra xem có tệp được tải lên hay không
        if (multipartFiles != null && multipartFiles.length > 0) {
            // Duyệt qua từng tệp tin trong mảng multipartFiles
            for (MultipartFile file : multipartFiles) {
                // Kiểm tra xem tệp có tồn tại và không rỗng
                if (file != null && !file.isEmpty()) {
                    try {
                        // Lấy tên tệp gốc và loại bỏ các ký tự không an toàn
                        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                        // Lấy phần mở rộng của tệp tin
                        String fileExtension = getFileExtension(filename);

                        // Kiểm tra xem phần mở rộng tệp tin có hợp lệ không (jpg, jpeg, png)
                        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png");
                        if (!allowedExtensions.contains(fileExtension.toLowerCase())) {
                            throw new AppException(ErrorCode.IMAGE_INVALID);
                        }

                        // Tải ảnh lên Cloudinary
                        Map result = cloudinaryService.upload(file, "product_images");
                        String cloudinaryUrl = (String) result.get("secure_url"); // Lấy URL từ kết quả trả về

                        // Sử dụng Admin API để lấy thông tin chi tiết của ảnh

                //        String publicId = (String) result.get("public_id");
                        // Thêm mã vào URL Cloudinary
                 //       String modifiedUrl = insertCodeIntoUrl(cloudinaryUrl, codeCount.get(i));

                        // Tạo đối tượng ProductImageDTO và đặt URL Cloudinary
                        ProductImageDTO dto = new ProductImageDTO();
                        dto.setImage_name(filename); // Sử dụng tên tệp gốc
                        dto.setFullPath(cloudinaryUrl); // Lưu trữ URL Cloudinary
                        dto.setFileOriginalName(FilenameUtils.removeExtension(filename)); // Tên tệp gốc (không phần mở rộng)
                        dto.setProduct_id(product_id); // ID của sản phẩm
                        productImageDTOs.add(dto); // Thêm vào danh sách DTO

                        // Tạo đối tượng Productimages và đặt URL Cloudinary
                        Productimages fileUpload = new Productimages();
                        fileUpload.setImage_name(filename);
                        fileUpload.setFileOriginalName(FilenameUtils.removeExtension(filename));
                        fileUpload.setExtension_name(fileExtension);
                        fileUpload.setFullPath(cloudinaryUrl); // Lưu URL Cloudinary
                        fileUpload.setProduct(products); // Liên kết ảnh với sản phẩm
//                        fileUpload.setCode(codeCount.get(i));
                        fileUploads.add(fileUpload); // Thêm vào danh sách lưu trữ
                      //  i++;
                    } catch (IOException ex) { // Xử lý ngoại lệ nếu có lỗi xảy ra trong quá trình tải lên
                        throw new AppException(ErrorCode.IMAGE_INVALID);
                    }
                }

            }

            // Lưu tất cả các đối tượng Productimages vào cơ sở dữ liệu
            productImageRepository.saveAll(fileUploads);

            // Trả về danh sách ProductImageDTO
            return productImageDTOs;
        } else {
            return new ArrayList<>(); // Trả về danh sách rỗng nếu không có tệp
        }
    }
    @Override
    public Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFile) {
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

            // Upload to Cloudinary
            Map result = cloudinaryService.upload(multipartFile, "product_images"); // Folder for thumbnails
            String cloudinaryUrl = (String) result.get("secure_url");
         //   String id =(String) result.get("public_id");

            // Create and return Product_Thumbnail object
            Product_Thumbnail thumbnail = new Product_Thumbnail();
            thumbnail.setFullPath(cloudinaryUrl);
            return thumbnail;

        } catch (IOException ex) {
            throw new AppException(ErrorCode.IMAGE_INVALID);
        }
    }
    @Override
    public List<ProductImageDTO> uploadFile_Request(List<String> imageUrls, int requestId) {
        // Thay đổi đầu vào thành danh sách URL ảnh từ Cloudinary

        Requests request = requestRepository.findById(requestId);

        if (request == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        List<Requestimages> requestImages = new ArrayList<>();

        // Duyệt qua danh sách URL ảnh từ Cloudinary
        for (String imageUrl : imageUrls) {
            // Không cần kiểm tra định dạng tệp vì chúng ta đã có URL trên Cloudinary

            // Tạo đối tượng Requestimages từ URL ảnh Cloudinary
            Requestimages requestImage = new Requestimages();
            requestImage.setImage_name(FilenameUtils.getName(imageUrl)); // Lấy tên tệp từ URL
            requestImage.setFullPath(imageUrl); // Lưu trữ trực tiếp URL Cloudinary
            requestImage.setRequests(request);
            requestImages.add(requestImage);
        }

        requestimagesRepository.saveAll(requestImages);

        // Chuyển đổi danh sách Requestimages thành danh sách ProductImageDTO
        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Requestimages image : requestImages) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId());
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath()); // Đây là URL Cloudinary
            dto.setProduct_id(image.getRequests().getRequestId());
            productImageDTOs.add(dto);
        }

        return productImageDTOs;
    }

    @Override
    public List<ProductImageDTO> uploadFile_RequestProduct(List<String> imageUrls, int requestProductId) { // Nhận danh sách URL ảnh
        RequestProducts requestProducts = requestProductRepository.findById(requestProductId);

        if (requestProducts == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }

        List<Product_Requestimages> productRequestImages = new ArrayList<>();

        for (String imageUrl : imageUrls) {
            // Không cần kiểm tra định dạng file vì đã là URL trên Cloudinary

            Product_Requestimages productRequestImage = new Product_Requestimages();
            productRequestImage.setImage_name(FilenameUtils.getName(imageUrl)); // Lấy tên file từ URL
            productRequestImage.setFullPath(imageUrl); // Lưu URL Cloudinary
            productRequestImage.setRequestProducts(requestProducts);
            productRequestImages.add(productRequestImage);
        }

        productRequestimagesRepository.saveAll(productRequestImages);

        List<ProductImageDTO> productImageDTOs = new ArrayList<>();
        for (Product_Requestimages image : productRequestImages) {
            ProductImageDTO dto = new ProductImageDTO();
            dto.setProductImageId(image.getProductImageId());
            dto.setImage_name(image.getImage_name());
            dto.setFullPath(image.getFullPath()); // Đây là URL Cloudinary
            dto.setProduct_id(image.getRequestProducts().getRequestProductId());
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
    }
//    @Transactional
//    public HashMap<Integer, String> generateMultipleCode(int number) {
//        HashMap<Integer, String> codeMap = new HashMap<>();
//        LocalDate today = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
//        String dateString = today.format(formatter);
//        Productimages lastp = productImageRepository.findProductimagesTop(dateString + "PRI");
//        int count = lastp != null ? Integer.parseInt(lastp.getCode().substring(9)) : 0;
//        for (int i = 0; i < number; i++) {
//            count++;
//            String code = dateString + "PRI" + String.format("%03d", count);
//            codeMap.put(i + 1, code);
//        }
//        return codeMap;
//    }

//    // Hàm thêm mã vào URL Cloudinary
//    private String insertCodeIntoUrl(String url, String code) {
//        String[] parts = url.split("\\."); // Tách URL thành các phần dựa trên dấu chấm
//        if (parts.length > 1) { // Đảm bảo URL có phần mở rộng
//            String baseUrl = String.join(".", Arrays.copyOfRange(parts, 0, parts.length - 1)); // Lấy phần URL trước phần mở rộng
//            String extension = parts[parts.length - 1]; // Lấy phần mở rộng
//            return baseUrl + "_" + code + "." + extension; // Kết hợp lại URL với mã
//        }
//        return url; // Trả về URL gốc nếu không có phần mở rộng
//    }

}
