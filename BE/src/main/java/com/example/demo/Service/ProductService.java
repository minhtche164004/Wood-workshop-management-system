package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Entity.Products;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductService {
    Products AddNewProduct(ProductDTO productDTO) ;
  //  String uploadImage(MultipartFile file, int id) throws IOException;
//    byte[] dowloadImage(String fileName);
//    List<String> uploadImagesList(List<MultipartFile> files, int id) throws IOException;
//    List<byte[]> downloadImagesByProductList(int productId);
}
