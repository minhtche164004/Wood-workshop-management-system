package com.example.demo.Controllers.OderController;

import com.example.demo.Dto.ProductDTO.RequestAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.RequestProducts;
import com.example.demo.Entity.Requests;
import com.example.demo.Entity.WhiteList;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Repository.ProductImageRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        ApiResponse<WhiteList> apiResponse = new ApiResponse<>();
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
        apiResponse.setResult("Chỉnh Đơn hàng thành công");
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
}
