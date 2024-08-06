package com.example.demo.Controller;

import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Status_Job;
import com.example.demo.Entity.Status_Order;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.OrderRepository;
import com.example.demo.Repository.Status_Job_Repository;
import com.example.demo.Repository.Status_Order_Repository;
import com.example.demo.Service.OrderService;
import com.example.demo.Service.PaymentService;
import com.example.demo.Service.VNPayService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VNPayService vnPayService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private Status_Order_Repository status_Order_Repository;

    @MockBean
    private Status_Job_Repository statusJobRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private JobRepository jobRepository;

    @Test
    void testSubmitOrder() throws Exception {
        when(vnPayService.createOrder(anyInt(), any(String.class), any(String.class))).thenReturn("http://fakeurl.com");

        mockMvc.perform(post("/api/auth/submitOrder")
                        .param("amount", "100")
                        .param("orderInfo", "Order123")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("http://fakeurl.com"));
    }




    @Test
    void testGetVnPayPaymentFailure() throws Exception {
        when(vnPayService.orderReturn(any())).thenReturn(0);

        mockMvc.perform(get("/api/auth/vnpay-payment")
                        .param("vnp_OrderInfo", "Order123")
                        .param("vnp_PayDate", "2024-08-01")
                        .param("vnp_TransactionNo", "Txn123")
                        .param("vnp_Amount", "10000")
                )
                .andExpect(status().isFound())
           ;
    }

    @Test
    void testGetTransactions() throws Exception {
        String jsonResponse = "{\"data\": {\"key\": \"value\"}}";
        when(paymentService.getHistoriesTransactions()).thenReturn(jsonResponse);

        mockMvc.perform(get("/api/auth/getTransaction"))
                .andExpect(status().isOk())
              ;
    }

    @Test
    void testGetQR() throws Exception {
        String qrCode = "fakeQrCode";
        when(paymentService.getQRCodeBanking(anyInt(), any(String.class))).thenReturn("{\"data\":{\"qrDataURL\":\"" + qrCode + "\"}}");

        mockMvc.perform(post("/api/auth/getQRBanking")
                        .param("amount", "100")
                        .param("orderInfo", "Order123")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(qrCode));
    }

    @Test
    void testGetQRForEmployee() throws Exception {
        String qrCode = "fakeQrCode";
        when(paymentService.getQRCodeBankingForEmployee(anyInt(), any(String.class), any(String.class), any(String.class), any(String.class)))
                .thenReturn("{\"data\":{\"qrDataURL\":\"" + qrCode + "\"}}");

        mockMvc.perform(post("/api/auth/getQRBankingForEmployee")
                        .param("amount", "100")
                        .param("accountNo", "123456")
                        .param("username", "testUser")
                        .param("bin_bank", "testBank")
                        .param("orderInfo", "Order123")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(qrCode));
    }

    @Test
    void testGetQR12() throws Exception {
        String qrCode = "fakeQrCode";
        when(paymentService.getQRCodeBankingString(anyInt(), any(String.class))).thenReturn(qrCode);

        mockMvc.perform(get("/api/auth/getqr")
                        .param("amount", "100")
                        .param("orderInfo", "Order123")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(qrCode));
    }
}