package com.example.demo.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class CloudinaryService {
    Cloudinary cloudinary;

    public Map getResourceByPublicId(String publicId) throws Exception {
        return cloudinary.api().resource(publicId, ObjectUtils.emptyMap());
    }

    public CloudinaryService() {
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("cloud_name", "dfclzlwzp");
        valuesMap.put("api_key", "461936211753842");
        valuesMap.put("api_secret", "sah2rtJmOlUr08UxezXXGwqwpY0");
        valuesMap.put("secure", "true");
        cloudinary = new Cloudinary(valuesMap);
    }

    public Map upload(MultipartFile multipartFile, String folder) throws IOException {
        File file = convert(multipartFile);
        Map map = ObjectUtils.asMap(
                "folder", folder
        );
        Map result = cloudinary.uploader().upload(file, map);

        if (!Files.deleteIfExists(file.toPath())) {
            throw new IOException("Failed to delete temporary file: " + file.getAbsolutePath());
        }
        return result;
    }



    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

//    public String extractPublicIdFromUrl(String url) throws Exception {
//        try {
//            Map result = cloudinary.api().resource(url, ObjectUtils.asMap("resource_type", "image"));
//            return (String) result.get("public_id");
//        } catch (Exception e) {
//            // Xử lý lỗi nếu có (ví dụ: tài nguyên không tồn tại, lỗi xác thực, ...)
//            throw new RuntimeException("Không thể lấy thông tin ảnh từ Cloudinary: " + e.getMessage(), e);
//        }
//    }

    public String extractPublicIdFromUrl(String url) {
        // Tìm vị trí của "product_images" trong URL
        int folderIndex = url.indexOf("product_images");

        if (folderIndex > 0) {
            // Trích xuất chuỗi từ "product_images" đến hết
            String publicIdWithFolder = url.substring(folderIndex);
            return FilenameUtils.removeExtension(publicIdWithFolder); // Loại bỏ phần mở rộng (nếu có)
        }
        throw new IllegalArgumentException("Invalid Cloudinary URL format");
    }

//
//    public Map deleteImage(String publicId) throws IOException {
//        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//    }
   //   cloudinary.uploader().destroy(String public_id, Map options);
//    public String deleteImageByPublicId(String publicId) {
//        try {
//            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//            if ("ok".equals(result.get("result"))) {
//                return "Image deleted successfully";
//            } else {
//                return "Error deleting image: " + result.get("error");
//            }
//        } catch (IOException e) {
//            return "Error deleting image: " + e.getMessage();
//        }
//    }



        private String apiSignRequest(Map<String, Object> paramsToSign, String apiSecret) {
            try {
                // Sắp xếp params theo thứ tự bảng chữ cái
                Map<String, Object> sortedParams = new TreeMap<>(paramsToSign);

                // Tạo chuỗi để ký
                StringBuilder toSign = new StringBuilder();
                for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
                    if (entry.getValue() instanceof Map || entry.getValue() instanceof Object[]) {
                        continue; // Bỏ qua các giá trị là Map hoặc mảng
                    }
                    toSign.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                toSign.append("api_secret=").append(apiSecret);

                // Tạo chữ ký SHA-1
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                byte[] digest = md.digest(toSign.toString().getBytes());
                StringBuilder signature = new StringBuilder();
                for (byte b : digest) {
                    signature.append(String.format("%02x", b));
                }

                return signature.toString();
            } catch (NoSuchAlgorithmException e) {
                // Xử lý ngoại lệ nếu không tìm thấy thuật toán SHA-1
                throw new RuntimeException("Error generating signature", e);
            }
        }

    public Map deleteImage(String publicId) throws IOException {
        // 1. Tạo timestamp (đơn vị giây)
        long timestamp = System.currentTimeMillis() / 1000L;

        // 2. Tạo params để ký
        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("public_id", publicId);
        paramsToSign.put("timestamp", timestamp);

        // 3. Tạo chữ ký bằng hàm đã có
        String signature = apiSignRequest(paramsToSign, cloudinary.config.apiSecret);

        // 4. Tạo options với chữ ký và timestamp
        Map options = ObjectUtils.asMap(
                "timestamp", timestamp,
                "signature", signature,
                "invalidate", true // Xóa cả trên CDN nếu cần
        );

        // 5. Gửi yêu cầu xóa ảnh
        return cloudinary.uploader().destroy(publicId, options);
    }

}
