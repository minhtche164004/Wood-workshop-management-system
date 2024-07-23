package com.example.demo.Controllers;

import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Status_Job;
import com.example.demo.Entity.Status_Order;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.Status_Job_Repository;
import com.example.demo.Repository.Status_Order_Repository;
import com.example.demo.Service.VNPayService;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@org.springframework.stereotype.Controller

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private Status_Order_Repository status_Order_Repository;
    @Autowired
    private Status_Job_Repository statusJobRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private JobRepository jobRepository;


    @GetMapping("")
    public String home() {
        return "src/main/resources/templates/index.html";
    }

    @PostMapping("/submitOrder")
    public ResponseEntity<Map<String, String>> submitOrder(@RequestParam("amount") int orderTotal,
                                                           @RequestParam("orderInfo") String orderInfo,
                                                           HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/api/auth";
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        Map<String, String> response = new HashMap<>();
        response.put("url", vnpayUrl);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/vnpay-payment")
//    public String GetMapping(HttpServletRequest request, Model model) {
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        String orderInfo = request.getParameter("vnp_OrderInfo");
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//
//        model.addAttribute("orderId", orderInfo);
//        model.addAttribute("totalPrice", totalPrice);
//        model.addAttribute("paymentTime", paymentTime);
//        model.addAttribute("transactionId", transactionId);
//        if(paymentStatus == 1) {
//            return "ordersuccess";
//        }else {
//            return "orderfail";
//        }
////        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
//    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<String> GetMapping(HttpServletRequest request) {

        int paymentStatus = vnPayService.orderReturn(request);
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        Orders orders = orderRepository.findByCode(orderInfo);

        String requestURL = request.getRequestURL().toString();
        String urlLocalSuccess = "http://localhost:5173/order-vnpay-success?"
                + "orderInfo=" + orderInfo
                + "&paymentTime=" + paymentTime
                + "&transactionId=" + transactionId
                + "&totalPrice=" + totalPrice;
        String urlDevelopSuccess = "https://dogosydungs.azurewebsites.net/order-vnpay-success?"
                + "orderInfo=" + orderInfo
                + "&paymentTime=" + paymentTime
                + "&transactionId=" + transactionId
                + "&totalPrice=" + totalPrice;;

        String urlLocalFail = "http://localhost:5173/order-vnpay-fail?"
                + "orderInfo=" + orderInfo
                + "&paymentTime=" + paymentTime
                + "&transactionId=" + transactionId
                + "&totalPrice=" + totalPrice;
        String urlDevelopFail = "https://dogosydungs.azurewebsites.net/order-vnpay-fail?"
                + "orderInfo=" + orderInfo
                + "&paymentTime=" + paymentTime
                + "&transactionId=" + transactionId
                + "&totalPrice=" + totalPrice;;

        BigDecimal totalPriceAsBigDecimal = new BigDecimal(totalPrice).divide(new BigDecimal(100));

        if (paymentStatus == 1 && orders != null && totalPriceAsBigDecimal.compareTo(orders.getDeposite()) == 0) {
            Status_Order statusOrder = new Status_Order();
            if(orders.getSpecialOrder() == false){//nếu là hàng có sẵn thì set status order cho nó là đã thi công xong luôn(vì nó ko cần sản xuất nữa)
                 statusOrder = status_Order_Repository.findById(4);
            }
            if(orders.getSpecialOrder() == true){//nếu là hàng đặt làm theo yêu cầu thì set status order cho nó là đã đặt cọc thành công
                 statusOrder = status_Order_Repository.findById(3);//đã đặt cọc, đang thi công
            }
            Status_Job statusJob = statusJobRepository.findById(3); // 3 la status job sau khi dat coc thi set status la chua giao viec
            List<Jobs> jobsList = jobRepository.getJobByOrderDetailByOrderCode(orderInfo);
            for(Jobs jobs : jobsList){
                jobs.setStatus(statusJob);
                jobRepository.save(jobs);
            }
            orders.setStatus(statusOrder);
            orderRepository.save(orders);

            if (requestURL.startsWith("http://localhost:8080") || requestURL.startsWith("https://localhost:8080")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(urlLocalSuccess))
                        .build();
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(urlDevelopSuccess))
                        .build();
            }

        } else {
            if (requestURL.startsWith("http://localhost:8080") || requestURL.startsWith("https://localhost:8080")) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(urlLocalFail))
                        .build();
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .location(URI.create(urlDevelopFail))
                        .build();
            }
        }

    }

//    @GetMapping("/getToken")
//    public ResponseEntity<String> getToken() {
//
//        String token = String.valueOf(paymentService.login());
//
//        return ResponseEntity.ok(token);
//    }

//    @GetMapping("/getTransaction")
//    public ResponseEntity<String> getTransactions() {
//
//        String info = String.valueOf(paymentService.getHistoriesTransactions());
//        return ResponseEntity.ok(info);
//    }


    @GetMapping("/getTransaction")
    public ResponseEntity<Map> getTransactions() {
        String info = paymentService.getHistoriesTransactions();
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(info, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(map);
    }

    //      lay response moi string cua qr dang string
    @PostMapping("/getQRBanking")
    public ResponseEntity<String> getQR(@RequestParam("amount") int amount, @RequestParam("orderInfo") String orderInfo) {
        String info = paymentService.getQRCodeBanking(amount, orderInfo);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(info, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
        String qrCode = (String) dataMap.get("qrDataURL");

        return ResponseEntity.ok(qrCode);
    }
    //      lay response moi string cua qr dang string


    @PostMapping("/getQRBankingForEmployee")
    public ResponseEntity<String> getQRBankingForEmployee(@RequestParam("amount") int amount,
                                                          @RequestParam("accountNo") String accountNo,//là stk ngân hàng
                                                          @RequestParam("username") String username,
                                                          @RequestParam("bin_bank") String bin_bank,
                                                          @RequestParam("orderInfo") String orderInfo) {
        String info = paymentService.getQRCodeBankingForEmployee(amount, accountNo, username, bin_bank, orderInfo);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(info, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
        String qrCode = (String) dataMap.get("qrDataURL");

        return ResponseEntity.ok(qrCode);
    }


    @GetMapping("/getqr")
    public ResponseEntity<String> getQR12(@RequestParam("amount") int amount, @RequestParam("orderInfo") String orderInfo) {
        return ResponseEntity.ok(paymentService.getQRCodeBankingString(amount, orderInfo));

    }

    // lay response cua qr dang json
//    @PostMapping ("/getQRBanking")
//    public ResponseEntity<Map<String, String>> getQR(@RequestParam("amount") int amount, @RequestParam("orderInfo") String orderInfo) {
//        String info = paymentService.getQRCodeBanking(amount, orderInfo);
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new HashMap<>();
//        try {
//            map = mapper.readValue(info, Map.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
//        String qrCode = (String) dataMap.get("qrDataURL");
//
//        Map<String, String> responseMap = new HashMap<>();
//        responseMap.put("qrCode", qrCode);
//
//        return ResponseEntity.ok(responseMap);
//    }

//
}
