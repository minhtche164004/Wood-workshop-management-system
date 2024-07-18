package com.example.demo.Dto.OrderDTO;

import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
@AllArgsConstructor
@Data
public class RequestProductWithFiles {
    private RequestProductDTO requestProductDTO;
    private MultipartFile[] files;
}
