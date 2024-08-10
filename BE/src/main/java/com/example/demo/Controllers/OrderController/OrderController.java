package com.example.demo.Controllers.OrderController;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.OrderDTO.*;

import com.example.demo.Dto.ProductDTO.*;
import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
import com.example.demo.Dto.RequestDTO.RequestEditDTO;
import com.example.demo.Dto.SubMaterialDTO.CreateExportMaterialProductRequestDTO;
import com.example.demo.Entity.*;

import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.Status_Order_Repository;

import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();

//    @GetMapping("/GetAllProductRequest")
//    public ApiResponse<?> GetAllProductRequest() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.GetAllProductRequest());
//        return apiResponse;
//    }
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

    @GetMapping("/TimeContract")
    public ApiResponse<?> TimeContract(@RequestParam("order_id") int order_id, @RequestParam("percentage_discount") double percentage_discount,@RequestParam("new_date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date new_date) throws MessagingException {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.TimeContract(order_id,percentage_discount,new_date));
        return apiResponse;

    }
//    @GetMapping("/GetAllRequestByAccountId")
//    public ApiResponse<?> GetAllRequestByAccountId(@RequestParam("acc_id") int acc_id) {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.GetAllRequestByAccountId(acc_id));
//        return apiResponse;
//
//    }
//    @GetMapping("/GetAllRequest")
//    public ApiResponse<?> GetAllRequest() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.GetAllRequests());
//        return apiResponse;
//
//    }
//    @GetMapping("/GetAllRequestAccept")
//    public ApiResponse<?> GetAllRequestAccept() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.GetAllRequestsAccept());
//        return apiResponse;
//
//    }
    @PutMapping(value ="/CustomerEditRequest" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> CustomerEditRequest(@RequestParam(value="request_id") int request_id,
                                              @RequestPart("requestEditCusDTO") RequestEditCusDTO requestEditCusDTO,
                                              @RequestPart("files") MultipartFile[] files) throws Exception {
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.EditRequest(request_id,requestEditCusDTO,files));
        return apiResponse;
    }
//    @GetMapping("/GetRequestById")
//    public ApiResponse<?> GetRequestById(@RequestParam("id") int id) {
//        ApiResponse<RequestAllDTO> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.GetRequestById(id));
//        return apiResponse;
//    }
    @GetMapping("/GetOrderById")
    public ApiResponse<?> GetOrderById(@RequestParam("id") int id) {
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getOrderById(id));
        return apiResponse;
    }
    @GetMapping("/getAllRefundStatus")
    public ApiResponse<?> getAllRefundStatus() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getAllRefundStatus());
        return apiResponse;
    }
    @GetMapping("/getRefundStatusById")
    public ApiResponse<?> getRefundStatusById(@RequestParam("refundId") int refundId) {
        ApiResponse<RefundStatus> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getRefundStatusById(refundId));
        return apiResponse;
    }
    @GetMapping("/getRequestProductById")
    public ApiResponse<?> GetRequestProductByIdWithImage(@RequestParam("id") int id) {
        ApiResponse<RequestProductDTO_Show> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetRequestProductByIdWithImage(id));
        return apiResponse;
    }
//    @PreAuthorize("hasAuthority('CUSTOMER')")
    @PostMapping("/AddWhiteList")
    public ApiResponse<?> AddWhiteList(@RequestParam("product_id") int product_id) {
        ApiResponse<WishList> apiResponse = new ApiResponse<>();
        apiResponse.setResult(whiteListService.AddWhiteList(product_id));
        return apiResponse;
    }

    @PostMapping("/Set_Deposite_Order")
    public ResponseEntity<String> Set_Deposite_Order(@RequestParam("order_id") int order_id,@RequestParam("deposite_price") int deposite_price) {
        //  ApiResponse<ResponseEntity<?>> apiResponse = new ApiResponse<>();
        //  apiResponse.setResult(orderService.Cancel_Order(order_id,special_order_id));
        return orderService.Set_Deposite_Order(order_id,deposite_price);
    }

    @PostMapping("/Cancel_Order")
    public ResponseEntity<String> Cancel_Order(@RequestParam("order_id") int order_id,@RequestParam("special_order_id") boolean special_order_id,
                                               @RequestBody String response) {
      //  ApiResponse<ResponseEntity<?>> apiResponse = new ApiResponse<>();
      //  apiResponse.setResult(orderService.Cancel_Order(order_id,special_order_id));
        return orderService.Cancel_Order(order_id,special_order_id,response);
    }

    @PostMapping("/Refund_Order")
    public ResponseEntity<String> Refund_Order(@RequestParam("order_id") int order_id,@RequestParam("special_order_id") boolean special_order_id,
                                               @RequestParam("refund_price") int refund_price,
                                               @RequestBody String response,
                                               @RequestParam("status_Id_Refund") int status_Id_Refund) {
        //  ApiResponse<ResponseEntity<?>> apiResponse = new ApiResponse<>();
        //  apiResponse.setResult(orderService.Cancel_Order(order_id,special_order_id));
        return orderService.Refund_Order(order_id,special_order_id, refund_price, response, status_Id_Refund);
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
    @PostMapping(value = "/AddNewRequestProduct", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewRequestProduct(
            @RequestBody RequestProductWithFiles[] requestProductsWithFiles,
            @RequestParam("order_id") int order_id
//            @RequestPart("files") MultipartFile[] files

    ) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddNewProductRequest(requestProductsWithFiles,order_id));
        return apiResponse;
    }
    @PostMapping("/Approve_Reject_Request")
    public ApiResponse<?> Approve_Reject_Request(@RequestParam("id") int id,@RequestParam("status_id") int status_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.Approve_Reject_Request(id,status_id);
        apiResponse.setResult("Chỉnh Yêu cầu đơn hàng thành công");
        return apiResponse;
    }

    @PostMapping("/ConfirmPayment")
    public ApiResponse<?> ConfirmPayment(@RequestParam("order_id") int order_id,@RequestParam("deposit") BigDecimal deposit) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.ConfirmPayment(order_id,deposit));
        return apiResponse;
    }
    @PostMapping ("/MultiFilterOrder")
    public ApiResponse<?> MultiFilterOrder(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer paymentMethod,
            @RequestParam(required = false)Integer specialOrder,
            @RequestBody(required = false) DateDTO dto
    ) {
        ApiResponse<List<OderDTO>> apiResponse = new ApiResponse<>(); // Chỉ định rõ kiểu List<OderDTO>
        apiResponse.setResult(orderService.MultiFilterOrder(search, statusId, paymentMethod,specialOrder, dto.getStartDate(), dto.getEndDate()));
        return apiResponse;
    }

    @PostMapping ("/MultiFilterOrderForCustomer")
    public ApiResponse<?> MultiFilterOrderForCustomer(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Integer paymentMethod,
            @RequestParam(required = false)Integer specialOrder,
            @RequestBody(required = false) DateDTO dto
    ) {
        ApiResponse<List<Orders>> apiResponse = new ApiResponse<>(); // Chỉ định rõ kiểu List<OderDTO>
        apiResponse.setResult(orderService.MultiFilterOrderForEmployee(search, statusId, paymentMethod,specialOrder, dto.getStartDate(), dto.getEndDate()));
        return apiResponse;
    }

    @PostMapping(value = "/AddNewRequest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> AddNewRequest(
            @RequestPart("requestDTO") RequestDTO requestDTO,
            @RequestPart("files") MultipartFile[] files
    ) {
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
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

    @GetMapping("GetAllOrderSpecial")
    public ApiResponse<?> GetAllOrderSpecial(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllOrderSpecial());
        return apiResponse;
    }

    @GetMapping("getAllOrderWithStatus3")
    public ApiResponse<?> getAllOrderWithStatus3(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderRepository.getAllOrderWithStatus3());
        return apiResponse;
    }

    @PostMapping("SendMailToNotifyCationAboutOrder")
    public ResponseEntity<Map<String, String>> SendMailToNotifyCationAboutOrder(@RequestParam("order_id") int order_id , HttpServletRequest request) throws MessagingException, IOException {
       // ApiResponse<String> apiResponse = new ApiResponse<>();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/auth";
        String vnpayUrl= orderService.SendMailToNotifyCationAboutOrder(order_id,baseUrl);
        Map<String, String> response = new HashMap<>();
        response.put("url", vnpayUrl);

        return ResponseEntity.ok(response);
    }

    @PutMapping("ManagerEditRequest")
    public ApiResponse<?> ManagerEditRequest(@RequestParam("request_id") int request_id,@RequestBody RequestEditDTO requestEditDTO){
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
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
    public ResponseEntity<ApiResponse<List<String>>> AddOrder(@RequestBody RequestOrder requestOrder) {
//        ApiResponse<Orders> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(orderService.AddOrder(requestOrder));
        return orderService.AddOrder(requestOrder);
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

    @GetMapping("/getAllOrderDetailOfProductByOrderId")
    public ApiResponse<?>  getAllOrderDetailOfProductByOrderId(@RequestParam("orderId") int orderId) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.getAllOrderDetailOfProductByOrderId(orderId));
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
        apiResponse.setResult("Xoá đơn hàng yêu cầu thành công!!!");
        return apiResponse;
    }

    @PutMapping("ChangeStatusOrder")
    public ApiResponse<?> ChangeStatusOrder(@RequestParam("orderId") int orderId,@RequestParam("status_id") int status_id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.ChangeStatusOrder(orderId,status_id);
      //  apiResponse.setResult("Sửa status của đơn hàng thành công");
        apiResponse.setResult(orderService.ChangeStatusOrder(orderId,status_id));
        return apiResponse;
    }

    @PutMapping("ChangeStatusOrderFinish")
    public ApiResponse<?> ChangeStatusOrderFinish(@RequestParam("orderId") int orderId,@RequestParam("status_id") int status_id, @RequestParam("remain_price")int remain_price){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.ChangeStatusOrder(orderId,status_id);
        //  apiResponse.setResult("Sửa status của đơn hàng thành công");
        BigDecimal remain_priceBigDecimal = new BigDecimal(remain_price);
        apiResponse.setResult(orderService.ChangeStatusOrderFinish(orderId,status_id, remain_priceBigDecimal));
        return apiResponse;
    }
}
