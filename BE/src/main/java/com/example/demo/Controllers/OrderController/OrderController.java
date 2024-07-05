package com.example.demo.Controllers.OrderController;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.OrderDTO.JobProductDTO;

import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailWithJobStatusDTO;
import com.example.demo.Dto.ProductDTO.ProductEditDTO;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Entity.*;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.Status_Order_Repository;

import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth/order/")
@AllArgsConstructor
public class OrderController {
    @Autowired
    private UserInforService userInforService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private Status_Order_Repository statusOrderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private WhiteListService whiteListService;
    private final OrderRepository orderRepository;

    @GetMapping("/GetAllProductRequest")
    public ApiResponse<?> GetAllProductRequest() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllProductRequest());
        return apiResponse;

    }
    @GetMapping("/GetAllProductRequestByUserId")
    public ApiResponse<?> GetAllProductRequestByUserId() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllProductRequestByUserId());
        return apiResponse;

    }
    @GetMapping("/GetAllRequestByUserId")
    public ApiResponse<?> GetAllRequestByUserId() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllRequestByUserId());
        return apiResponse;

    }
    @GetMapping("/GetAllRequest")
    public ApiResponse<?> GetAllRequest() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllRequests());
        return apiResponse;

    }
    @PutMapping(value ="/CustomerEditRequest" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> CustomerEditRequest(@RequestParam(value="request_id") int request_id,
                                              @RequestPart("requestEditCusDTO") RequestEditCusDTO requestEditCusDTO,
                                              @RequestPart("files") MultipartFile[] files) throws Exception {
        ApiResponse<Requests> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.EditRequest(request_id,requestEditCusDTO,files));
        return apiResponse;
    }
    @GetMapping("/GetRequestById")
    public ApiResponse<?> GetRequestById(@RequestParam("id") int id) {
        ApiResponse<RequestAllDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetRequestById(id));
        return apiResponse;
    }
    @GetMapping("/GetRequestProductById")
    public ApiResponse<?> GetRequestProductById(@RequestParam("id") int id) {
        ApiResponse<RequestProductAllDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetProductRequestById(id));
        return apiResponse;
    }
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/AddWhiteList")
    public ApiResponse<?> AddWhiteList(@RequestParam("product_id") int product_id) {
        ApiResponse<WishList> apiResponse = new ApiResponse<>();
        apiResponse.setResult(whiteListService.AddWhiteList(product_id));
        return apiResponse;
    }
    @GetMapping("/GetWhiteListByUser")
    public ApiResponse<?> GetWhiteListByUser() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(whiteListService.ViewWhiteList());
        return apiResponse;
    }
    @DeleteMapping("/DeleteWhiteList")
    public ApiResponse<?> DeleteWhiteList(@RequestParam("wishlist_id") int wishlist_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        whiteListService.DeleteWhiteList(wishlist_id);
        apiResponse.setResult("Xoá Sản Phẩm khỏi danh sách yêu thích thành công !");
        return apiResponse;
    }
    @PostMapping(value = "/AddNewRequestProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewRequestProduct(
            @RequestPart("productDTO") RequestProductDTO requestProductDTO,
            @RequestPart("files") MultipartFile[] files
    ) {
        ApiResponse<RequestProducts> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddNewProductRequest(requestProductDTO, files));
        return apiResponse;
    }
    @PostMapping("/Approve_Reject_Request")
    public ApiResponse<?> Approve_Reject_Request(@RequestParam("id") int id,@RequestParam("status_id") int status_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.Approve_Reject_Request(id,status_id);
        apiResponse.setResult("Chỉnh Yêu cầu đơn hàng thành công");
        return apiResponse;
    }

    @PostMapping(value = "/AddNewRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewRequest(
            @RequestPart("requestDTO") RequestDTO requestDTO,
            @RequestPart("files") MultipartFile[] files
    ) {
        ApiResponse<Requests> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddNewRequest(requestDTO, files));
        return apiResponse;
    }

//    @PutMapping(value = "/EditRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ApiResponse<?> EditRequest(
//            @RequestParam(value="request_id") int request_id,
//            @RequestPart("productDTO") RequestEditDTO requestEditDTO,
//            @RequestPart("files") MultipartFile[] files
//    ) throws IOException {
//        ApiResponse<Requests> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.EditRequest(request_id,requestEditDTO,files));
//        return apiResponse;
//    }


    @GetMapping("GetAllOrder")
    public ApiResponse<?> GetAllOrder(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllOrder());
        return apiResponse;
    }
    @PutMapping("ManagerEditRequest")
    public ApiResponse<?> ManagerEditRequest(@RequestParam("request_id") int request_id,@RequestBody RequestEditDTO requestEditDTO){
        ApiResponse<Requests> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.ManagerEditRequest(request_id,requestEditDTO));
        return apiResponse;
    }

    @GetMapping("FindOrderByNameorCode")
    public ApiResponse<?> FindOrderByNameorCode(@RequestParam("key") String key){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.FindByNameOrCode(key));
        return apiResponse;
    }

    @PostMapping ("/AddOrder")
    public ApiResponse<?> AddOrder(@RequestBody RequestOrder requestOrder) {
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddOrder(requestOrder));
        return apiResponse;
    }

    @GetMapping("/filter-by-date")
    public ApiResponse<?>  getOrdersByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.FilterByDate(from,to));
        return apiResponse;
    }
    @GetMapping("/filter-by-status")
    public ApiResponse<?>  FilterByStatus(
           @RequestParam("status_id") int status_id){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.FilterByStatus(status_id));
        return apiResponse;
    }

    @GetMapping("/historyOrder")
    public ApiResponse<?>  historyOrder() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.HistoryOrder());
        return apiResponse;
    }
    @GetMapping("/getOrderDetailById")
    public ApiResponse<?>  getOrderDetailById(@RequestParam("id") int id) {
        ApiResponse<OrderDetailDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getOrderDetailById(id));
        return apiResponse;
    }

    @GetMapping("/getStatusOrder")
    public ApiResponse<?>  getStatusOrder() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statusOrderRepository.findAll());
        return apiResponse;
    }

    @GetMapping("/getAllOrderDetail")
    public ApiResponse<?>  getAllOrderDetail() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getAllOrderDetail());
        return apiResponse;
    }
    @GetMapping("/getAllOrderDetailByOrderId")
    public ApiResponse<?>  getAllOrderDetail(@RequestParam("orderId") int orderId) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getOrderDetailByOrderId(orderId));
        return apiResponse;
    }
    @GetMapping("/getListPhoneNumber")
    public ApiResponse<?>  getListPhoneNumber() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userInforService.listPhoneNumberHasAccount());
        return apiResponse;
    }
    @GetMapping("/getInfoUserByPhoneNumber")
    public ApiResponse<?>  getInfoUserByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(userInforService.getUserInforByPhoneNumber(phoneNumber));
        return apiResponse;
    }
    @DeleteMapping("/deleteRequest")
    public ApiResponse<?> deleteRequest(@RequestParam("requestId") int requestId){
        ApiResponse apiResponse = new ApiResponse<>();
        orderService.deleteRequestById(requestId);
        apiResponse.setResult("Xoá yêu cầu thành công!!!");
        return apiResponse;
    }

    @PutMapping("ChangeStatusOrder")
    public ApiResponse<?> ChangeStatusOrder(@RequestParam("orderId") int orderId,@RequestParam("status_id") int status_id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.ChangeStatusOrder(orderId,status_id);
        apiResponse.setResult("Sửa status của đơn hàng thành công");
        return apiResponse;
    }
}
