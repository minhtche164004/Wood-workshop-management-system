package com.example.demo.Service;

import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Entity.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public interface OrderService {
    Orders AddOrder(RequestOrder requestOrder);

    List<RequestProducts> GetAllProductRequest();

    List<Requests> GetAllRequests();

    Requests getRequestById(int id);

    RequestProducts getRequestProductsById(int id);

    void Approve_Reject_Request(int id, int status_id);

    RequestProductAllDTO GetProductRequestById(int id);

    RequestAllDTO GetRequestById(int id);

    List<Orders> GetAllOrder();

    List<Orders> FindByNameOrCode(String key);

    List<Orders> FilterByDate(Date from, Date to);

    List<Orders> FilterByStatus(int status_id);

    List<Orders> HistoryOrder();

    List<Orderdetails> getAllOrderDetail();

    RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles);

    Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles);

//    Requests EditRequest(int request_id, RequestEditDTO requestEditDTO,MultipartFile[] multipartFiles) throws IOException;

    public List<OrderDetailWithJobStatusDTO> getOrderDetailByOrderId(int order_id);

    Requests ManagerEditRequest(int request_id, RequestEditDTO requestEditDTO);
    OrderDetailDTO getOrderDetailById(int id);

}