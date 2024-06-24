package com.example.demo.Controllers.OrderController;

import com.example.demo.Dto.RequestDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.OrderDTO.RequestOrder;
import com.example.demo.Entity.Orders;
import com.example.demo.Entity.RequestProducts;
import com.example.demo.Entity.Requests;
import com.example.demo.Entity.WishList;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/auth/order/")
@AllArgsConstructor
public class OrderController {
@Autowired
private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WhiteListService whiteListService;

    @GetMapping("/GetAllProductRequest")
    public ApiResponse<?> GetAllProductRequest() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllProductRequest());
        return apiResponse;

    }
    @GetMapping("/GetAllRequest")
    public ApiResponse<?> GetAllRequest() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllRequests());
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
    @PostMapping("/AddWhiteList")
    public ApiResponse<?> AddWhiteList(@RequestParam("product_id") int product_id) {
        ApiResponse<WishList> apiResponse = new ApiResponse<>();
        apiResponse.setResult(whiteListService.AddWhiteList(product_id));
        return apiResponse;
    }
    @GetMapping("/GetWhiteListByUserID")
    public ApiResponse<?> GetWhiteListByUserID(@RequestParam("user_id") int user_id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(whiteListService.ViewWhiteList(user_id));
        return apiResponse;
    }
    @DeleteMapping("/DeleteWhiteList")
    public ApiResponse<?> DeleteWhiteList(@RequestParam("user_id") int user_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        whiteListService.DeleteWhiteList(user_id);
        apiResponse.setResult("Xoá Sản Phẩm khỏi danh sách yêu thích thành công !");
        return apiResponse;
    }
    @PostMapping(value = "/AddNewRequestProduct")
    public ApiResponse<?> AddNewRequestProduct(
            @RequestBody RequestProductDTO requestProductDTO

    ) {
        ApiResponse<RequestProducts> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddNewProductRequest(requestProductDTO));
        return apiResponse;
    }
    @PostMapping("/Approve_Reject_Request")
    public ApiResponse<?> Approve_Reject_Request(@RequestParam("id") int id,@RequestParam("status_id") int status_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderService.Approve_Reject_Request(id,status_id);
        apiResponse.setResult("Chỉnh Đơn hàng thành công");
        return apiResponse;
    }

    @PostMapping(value = "/AddNewRequest")
    public ApiResponse<?> AddNewRequest(
            @RequestBody RequestDTO requestDTO

    ) {
        ApiResponse<Requests> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.AddNewRequest(requestDTO));
        return apiResponse;
    }
    @GetMapping("GetAllOrder")
    public ApiResponse<?> GetAllOrder(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(orderService.GetAllOrder());
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
}
