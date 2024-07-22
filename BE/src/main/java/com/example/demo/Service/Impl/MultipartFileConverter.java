package com.example.demo.Service.Impl;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

@Service
public class MultipartFileConverter {

    public MultipartFile convertBase64ToMultipartFile(String base64, String originalFileName) {
        // Tách thông tin header và dữ liệu Base64
        String[] parts = base64.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid Base64 string: missing comma or data");
        }
        String dataPrefix = parts[0];
        String imageDataBytes = parts[1];

        // Xác định content type và file extension
        String contentType = dataPrefix.split(":")[1].split(";")[0];
        String fileExtension = FilenameUtils.getExtension(contentType).toLowerCase();

        // Kiểm tra content type
        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid Base64 string: not an image");
        }

        // Tạo tên file ngẫu nhiên nếu originalFilename là null
        String fileName = (originalFileName != null && !originalFileName.isEmpty())
                ? originalFileName
                : UUID.randomUUID().toString() + "." + fileExtension;
        byte[] decodedBytes = Base64.getDecoder().decode(imageDataBytes);
        return new CustomMultipartFile(decodedBytes, originalFileName);
    }
}

