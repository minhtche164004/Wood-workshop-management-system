package com.example.demo.Controllers;

import com.example.demo.Config.VNPayService;
import com.example.demo.Service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@org.springframework.stereotype.Controller

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class PaymentController {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private PaymentService paymentService;


    @GetMapping("")
    public String home() {
        return "src/main/resources/templates/index.html";
    }

    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    public String GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
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
//    @PostMapping ("/getQRBanking")
//    public ResponseEntity<String> getQR(@RequestParam("amount") int amount, @RequestParam("orderInfo") String orderInfo) {
//        String info = paymentService.getQRCodeBanking(amount, orderInfo);
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = new HashMap<>();
//        try {
//            map = mapper.readValue(info, Map.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
//        String qrCode = (String) dataMap.get("qrCode");
//
//        return ResponseEntity.ok(qrCode);
//    }

    // lay response cua qr dang json
    @PostMapping ("/getQRBanking")
    public ResponseEntity<Map<String, String>> getQR(@RequestParam("amount") int amount, @RequestParam("orderInfo") String orderInfo) {
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

        Map<String, String> responseMap = new HashMap<>();
        responseMap.put("qrCode", qrCode);

        return ResponseEntity.ok(responseMap);
    }
}
