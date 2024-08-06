package com.example.demo;
import com.example.demo.Dto.OrderDTO.*;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.CloudinaryService;
import com.example.demo.Service.Impl.MultipartFileConverter;
import com.example.demo.Service.Impl.OrderServiceImpl;
import com.example.demo.Service.UploadImageService;
import com.example.demo.Service.VNPayService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private Status_Order_Repository statusOrderRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private InformationUserRepository informationUserRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UploadImageService uploadImageService;
    @Mock
    private RequestProductRepository requestProductRepository;
    @Mock
    private Product_RequestimagesRepository productRequestimagesRepository;
    @Mock
    private RequestimagesRepository requestimagesRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private Status_Job_Repository statusJobRepository;
    @Mock
    private EntityManager entityManager;
    @Mock
    private CloudinaryService cloudinaryService;
    @Mock
    private EmailService emailService;
    @Mock
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Mock
    private Status_Product_Repository statusProductRepository;
    @Mock
    private ProcessproducterrorRepository processproducterrorRepository;
    @Mock
    private InputSubMaterialRepository inputSubMaterialRepository;
    @Mock
    private SubMaterialsRepository subMaterialsRepository;
    @Mock
    private MultipartFileConverter multipartFileConverter;
    @Mock
    private VNPayService vnPayService;
    @Mock
    private CheckConditionService checkConditionService;
    public OrderServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testMultiFilterOrder_NoResult() {
        // Given
        String search = "test";
        Integer statusId = 1;
        Integer paymentMethod = 1;
        Integer specialOrder = -1;
        Date startDate = new Date();
        Date endDate = new Date();
        when(orderRepository.MultiFilterOrder(search, statusId, paymentMethod, startDate, endDate))
                .thenReturn(new ArrayList<>()); // No results

        // When & Then
        assertThrows(AppException.class, () -> orderService.MultiFilterOrder(search, statusId, paymentMethod, specialOrder, startDate, endDate));
        verify(orderRepository, times(1)).MultiFilterOrder(search, statusId, paymentMethod, startDate, endDate);
        verify(orderRepository, times(0)).MultiFilterOrderSpecialOrder(anyString(), anyInt(), anyInt(), anyBoolean(), any(), any());
        verify(orderRepository, times(0)).MultiFilterOrderWithoutOrderType(anyString(), anyInt(), anyInt(), any(), any());
    }


}
