package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.ProductImageDTO;
import com.example.demo.Dto.ProductDTO.Product_Thumbnail;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface UploadImageService {
    //up cho product
//    List<ProductImageDTO> uploadFile(MultipartFile[] multipartFiles, int product_id);
    //up cho requestProduct
//    List<ProductImageDTO> uploadFile1(MultipartFile[] multipartFiles, int requestProduct_id);
//    List<ProductImageDTO> uploadFile2(MultipartFile[] multipartFiles, int request_id);
//     Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFiles);



//    Product_Thumbnail uploadFile_Thumnail(String imageName);
//
//    List<ProductImageDTO> uploadFile(List<String> imageNames, int product_id);
    List<ProductImageDTO> uploadFile_Request(List<String> imageNames, int requestId);
    List<ProductImageDTO> uploadFile_RequestProduct(List<String> imageNames, int requestProductId);

    List<ProductImageDTO> uploadFile(MultipartFile[] multipartFiles, int product_id);
    Product_Thumbnail uploadFile_Thumnail(MultipartFile multipartFiles);

    List<ProductImageDTO> uploadFileRequestProduct(MultipartFile[] multipartFiles, int requestProduct_id);
    List<ProductImageDTO> uploadFileRequest(MultipartFile[] multipartFiles, int request_id);
}
