package com.example.demo.Service;

import com.example.demo.Dto.OrderDTO.*;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Dto.SubMaterialDTO.CreateExportMaterialProductRequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Response.ApiResponse;
import jakarta.mail.MessagingException;
import org.junit.runner.Request;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public interface OrderService {
    ResponseEntity<ApiResponse<List<String>>> AddOrder(RequestOrder requestOrder);

    List<RequestProductAllDTO> GetAllProductRequest();

    //  List<Orders> GetAllRequests();
    // List<Orders> GetAllRequestsAccept();

    //  Orders getRequestById(int id);

    RequestProducts getRequestProductsById(int id);

    void Approve_Reject_Request(int id, int status_id);

    //  RequestProductAllDTO GetProductRequestById(int id);

    // RequestAllDTO GetRequestById(int id);

    List<OderDTO> GetAllOrder();

    List<OderDTO> GetAllOrderSpecial();

    List<OderDTO> FindByNameOrCode(String key);

    List<OderDTO> FilterByDate(Date from, Date to);

    List<OderDTO> FilterByStatus(int status_id);

    List<Orders> HistoryOrder();

    Orders getOrderById(int order_id);

    List<Orderdetails> getAllOrderDetail();

    List<RequestProducts> AddNewProductRequest(RequestProductWithFiles[] requestProductsWithFiles, int order_id);

    Orders AddNewRequest(RequestDTO requestDTO, MultipartFile[] multipartFiles);

//    Requests EditRequest(int request_id, RequestEditDTO requestEditDTO,MultipartFile[] multipartFiles) throws IOException;

    public List<OrderDetailWithJobStatusDTO> getOrderDetailByOrderId(int order_id);

    List<OrderDetailWithJobStatusDTO> getAllOrderDetailOfProductByOrderId(int order_id);

    Orders EditRequest(int request_id, RequestEditCusDTO requestEditDTO, MultipartFile[] multipartFiles) throws IOException;

    Orders ManagerEditRequest(int request_id, RequestEditDTO requestEditDTO);
//    Requests CustomerEditRequest(int request_id , Requests requests);

    OrderDetailDTO getOrderDetailById(int id);
//    RequestEditCusDTO getRequestEditCusDTOById(int id);

    void deleteRequestById(int requestId);

    String ChangeStatusOrder(int orderId, int status_id);

    String ChangeStatusOrderFinish(int orderId, int status_id, BigDecimal remain_price);

    List<RequestProductAllDTO> filterRequestProductsForAdmin(String search, Integer statusId, BigDecimal minPrice, BigDecimal maxPrice, String sortDirection);

    List<RequestProducts> findByPriceRange(BigDecimal min, BigDecimal max);

    List<RequestProducts> GetAllProductRequestByUserId();

    List<Orders> GetAllRequestByUserId();
    // List<Orders> GetAllRequestByAccountId(int acc_id);

    RequestProductDTO_Show GetRequestProductByIdWithImage(int id);

    List<RequestProductDTO_Show> GetAllRequestProductWithImage();

    ResponseEntity<String> Set_Deposite_Order(int order_id, int deposite_price);

    ResponseEntity<String> Cancel_Order(int order_id, boolean special_order_id, String response);

    ResponseEntity<String> Refund_Order(int order_id, boolean special_order_id, int refund_price, String response, int status_Id_Refund);


    String ConfirmPayment(int order_id, BigDecimal deposit);


    List<OderDTO> MultiFilterOrder(String search, Integer status_id, Integer paymentMethod, Integer specialOrder, Date startDate, Date endDate);

    List<Orders> MultiFilterOrderForEmployee(String search, Integer status_id, Integer paymentMethod, Integer specialOrder, Date startDate, Date endDate);


    String SendMailToNotifyCationAboutOrder(int order_id, String link) throws MessagingException, IOException;

    String RefundDeposite(int order_id);
<<<<<<< HEAD
    String TimeContract(int order_id, double percentage_discount,Date NewDate) throws MessagingException;


    List<RefundStatus> getAllRefundStatus();
    RefundStatus getRefundStatusById(int refundId);


=======
>>>>>>> fb8a4870e64e0bd6d658e72fe6e2b14a7b3f59e5

    String TimeContract(int order_id, double percentage_discount, Date NewDate) throws MessagingException;


}