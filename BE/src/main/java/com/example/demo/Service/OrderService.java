package com.example.demo.Service;

import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.RequestOrder;
import com.example.demo.Entity.*;
import com.example.demo.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    Orders AddOrder(RequestOrder requestOrder);
    RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles);
    Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles);
    List<RequestProducts> GetAllProductRequest();
    List<Requests> GetAllRequests();
    Requests getRequestById(int id);
    RequestProducts getRequestProductsById(int id);
    void Approve_Reject_Request(int id,int status_id);
    RequestProductAllDTO GetProductRequestById(int id);
    RequestAllDTO GetRequestById(int id);



}
