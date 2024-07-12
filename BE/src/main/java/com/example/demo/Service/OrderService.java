package com.example.demo.Service;

import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Entity.*;
import org.junit.runner.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
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

  //  RequestProductAllDTO GetProductRequestById(int id);

    RequestAllDTO GetRequestById(int id);

    List<Orders> GetAllOrder();

    List<Orders> FindByNameOrCode(String key);

    List<Orders> FilterByDate(Date from, Date to);

    List<Orders> FilterByStatus(int status_id);

    List<Orders> HistoryOrder();
    Orders getOrderById(int order_id);

    List<Orderdetails> getAllOrderDetail();

    RequestProducts AddNewProductRequest(RequestProductDTO requestProductDTO, MultipartFile[] multipartFiles);

    Requests AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles);

//    Requests EditRequest(int request_id, RequestEditDTO requestEditDTO,MultipartFile[] multipartFiles) throws IOException;

    public List<OrderDetailWithJobStatusDTO> getOrderDetailByOrderId(int order_id);

    Requests EditRequest(int request_id, RequestEditCusDTO requestEditDTO, MultipartFile[] multipartFiles) throws IOException;
    Requests ManagerEditRequest(int request_id, RequestEditDTO requestEditDTO);
//    Requests CustomerEditRequest(int request_id , Requests requests);

    OrderDetailDTO getOrderDetailById(int id);
//    RequestEditCusDTO getRequestEditCusDTOById(int id);

    void deleteRequestById(int requestId);

    void ChangeStatusOrder(int orderId, int status_id);


    List<RequestProductDTO_Show> filterRequestProductsForAdmin(String search,Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection);

    List<RequestProducts> findByPriceRange(BigDecimal min, BigDecimal max);
    List<RequestProducts> GetAllProductRequestByUserId();
    List<Requests> GetAllRequestByUserId();
    List<Requests> GetAllRequestByAccountId(int acc_id);

    RequestProductDTO_Show GetRequestProductByIdWithImage(int id);
    List<RequestProductDTO_Show> GetAllRequestProductWithImage();

   String  Cancel_Order(int order_id,boolean special_order_id);





}