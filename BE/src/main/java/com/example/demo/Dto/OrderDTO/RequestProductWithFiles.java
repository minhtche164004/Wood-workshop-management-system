package com.example.demo.Dto.OrderDTO;

import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data

public class RequestProductWithFiles {
    private RequestProductDTO requestProductDTO;
    private List<String> filesBase64;
}
//private MultipartFile[] files;