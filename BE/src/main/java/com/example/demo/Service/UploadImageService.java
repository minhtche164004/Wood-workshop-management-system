package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface UploadImageService {
    //up cho product
    List<ProductImageDTO> uploadFile(MultipartFile[] multipartFiles, int product_id);
    //up cho requestProduct
    List<ProductImageDTO> uploadFile1(MultipartFile[] multipartFiles, int product_id);

     Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFiles);
}
