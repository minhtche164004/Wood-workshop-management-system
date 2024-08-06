package com.example.demo;
import com.example.demo.Dto.OrderDTO.*;
import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Dto.ProductDTO.RequestProductDTO;
import com.example.demo.Dto.RequestDTO.RequestDTO;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
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
import com.example.demo.Service.Impl.ProductServiceImpl;
import com.example.demo.Service.UploadImageService;
import com.example.demo.Service.VNPayService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    private OrderServiceImpl orderServiceImpl;
    private ProductServiceImpl productRequestService;

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
    @BeforeEach
    public void setUp() {
        // Initialize mocks and the service
        orderServiceImpl = new OrderServiceImpl();
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        // Set up the authentication mock
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Set up the security context mock
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Set the mock security context into SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);
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

        //Kiểm tra xem phương thức orderService.MultiFilterOrder ném ra ngoại lệ AppException khi không tìm thấy kết quả
        assertThrows(AppException.class, () -> orderService.MultiFilterOrder(search, statusId, paymentMethod, specialOrder, startDate, endDate));
        //kiểm tra xem phương thức orderRepository.MultiFilterOrder được gọi đúng 1 lần với các tham số đã cho.
        verify(orderRepository, times(1)).MultiFilterOrder(search, statusId, paymentMethod, startDate, endDate);
        //Kiểm tra xem phương thức orderRepository.MultiFilterOrderSpecialOrder không được gọi
        verify(orderRepository, times(0)).MultiFilterOrderSpecialOrder(anyString(), anyInt(), anyInt(), anyBoolean(), any(), any());
        //Kiểm tra xem phương thức orderRepository.MultiFilterOrderWithoutOrderType không được gọi
        verify(orderRepository, times(0)).MultiFilterOrderWithoutOrderType(anyString(), anyInt(), anyInt(), any(), any());
    }

    @Test
    public void testAddOrder_NullInputs() {
        try {
            orderService.AddOrder(null);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // Expected exception
        }
    }





    @Test
    void testFilterByDate_InvalidDateRange() {
        Date from = new Date();
        Date to = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000); // yesterday

        assertThatThrownBy(() -> orderService.FilterByDate(from, to))
                .isInstanceOf(AppException.class)
                .hasMessage("Không tìm thấy kết quả tìm kiếm ");
    }






    @Test
    void testGetOrderById_OrderNotFound() {
        int orderId = 1;

        when(orderRepository.findById(anyInt())).thenReturn(null);

        assertThatThrownBy(() -> orderService.getOrderById(orderId))
                .isInstanceOf(AppException.class)
                .hasMessage("Không tìm thấy kết quả tìm kiếm ");
    }









//    @Test
//    void testAddRequestImages_UploadError() throws IOException {
//        List<MultipartFile> files = new ArrayList<>();
//        int requestId = 1;
//
//        doThrow(new IOException("Upload error")).when(uploadImageService).uploadFileRequest(any(MultipartFile[].class), anyInt());
//
//        assertThatThrownBy()
//                .isInstanceOf(AppException.class)
//                .hasMessage("Upload error");
//    }

    @Test
    void testHandleException_CustomException() {
        // Example: Custom exception handling if applicable
    }




    @Test
    public void testGetRequestProductsById_found() {
        // Arrange
        int id = 1;
        RequestProducts requestProducts = new RequestProducts();
        when(requestProductRepository.findById(id)).thenReturn(requestProducts);

        // Act
        RequestProducts result = orderService.getRequestProductsById(id);

        // Assert
        assertNotNull(result);
    }



    @Test
    public void testApproveRejectRequest_success() {
        // Arrange
        int id = 1;
        int statusId = 2;

        // Act
        orderService.Approve_Reject_Request(id, statusId);

        // Assert
        verify(orderRepository, times(1)).updateStatus(id, statusId);
    }

    @Test
    public void testAddNewProductRequest_success() {
        // Arrange
        RequestProductWithFiles[] requestProductsWithFiles = new RequestProductWithFiles[0];
        int orderId = 1;
        Orders order = new Orders();
        Status_Order statusOrder = new Status_Order();
        when(orderRepository.findById(orderId)).thenReturn(order);
        when(statusOrderRepository.findById(1)).thenReturn(statusOrder);

        RequestProducts requestProducts = new RequestProducts();
        when(requestProductRepository.save(any(RequestProducts.class))).thenReturn(requestProducts);
        when(orderRepository.save(any(Orders.class))).thenReturn(order);

        // Act
        List<RequestProducts> result = orderService.AddNewProductRequest(requestProductsWithFiles, orderId);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Orders.class));
    }



    @Test
    public void testFilterRequestProductsForAdmin_noSearchParams() {
        // Arrange
        when(requestProductRepository.getAllRequestProduct()).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.filterRequestProductsForAdmin(null, null, null, null, null);
        });
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }


    @Test
    public void testGetAllRequestByUserId_notFound() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.getUserByUsername("testUser")).thenReturn(user);

        when(orderRepository.findByUserId(user.getUserId())).thenReturn(null);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.GetAllRequestByUserId();
        });
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }
    @Test
    public void testAddNewProductRequest_invalidName() {
        // Arrange
        int orderId = 1;
        RequestProductWithFiles[] requestProductsWithFiles = new RequestProductWithFiles[1];
        RequestProductDTO requestProductDTO = new RequestProductDTO();
        requestProductDTO.setRequestProductName("InvalidName$");
        requestProductDTO.setDescription("Valid Description");
        requestProductDTO.setPrice(BigDecimal.valueOf(100));
        requestProductDTO.setQuantity(10);
        requestProductDTO.setCompletionTime(new java.sql.Date(System.currentTimeMillis()));

        requestProductsWithFiles[0] = new RequestProductWithFiles(requestProductDTO, Collections.singletonList("base64image"));

        Orders orders = new Orders();
        when(orderRepository.findById(orderId)).thenReturn(orders);
        when(checkConditionService.checkInputName(anyString())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.AddNewProductRequest(requestProductsWithFiles, orderId);
        });
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode());
        verify(orderRepository, times(1)).findById(orderId);
        verify(requestProductRepository, times(0)).save(any(RequestProducts.class));
        verify(uploadImageService, times(0)).uploadFileRequestProduct(any(MultipartFile[].class), anyInt());
    }

    @Test
    public void testAddNewProductRequest_invalidQuantity() {
        // Arrange
        int orderId = 1;
        RequestProductWithFiles[] requestProductsWithFiles = new RequestProductWithFiles[1];
        RequestProductDTO requestProductDTO = new RequestProductDTO();
        requestProductDTO.setRequestProductName("ValidName");
        requestProductDTO.setDescription("Valid Description");
        requestProductDTO.setPrice(BigDecimal.valueOf(100));
        requestProductDTO.setQuantity(-10); // Invalid quantity
        requestProductDTO.setCompletionTime(new java.sql.Date(System.currentTimeMillis()));

        requestProductsWithFiles[0] = new RequestProductWithFiles(requestProductDTO, Collections.singletonList("base64image"));

        Orders orders = new Orders();
        when(orderRepository.findById(orderId)).thenReturn(orders);
        when(checkConditionService.checkInputName(anyString())).thenReturn(true);
        when(checkConditionService.checkInputQuantityInt(anyInt())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.AddNewProductRequest(requestProductsWithFiles, orderId);
        });
        assertEquals(ErrorCode.QUANTITY_INVALID, thrown.getErrorCode());
        verify(orderRepository, times(1)).findById(orderId);
        verify(requestProductRepository, times(0)).save(any(RequestProducts.class));
        verify(uploadImageService, times(0)).uploadFileRequestProduct(any(MultipartFile[].class), anyInt());
    }
    @Test
    public void testAddNewRequest_invalidName() {
        // Arrange
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFullname("InvalidName$");
        requestDTO.setDescription("Description");
        requestDTO.setAddress("Address");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setCity_province("City");
        requestDTO.setDistrict_province("District");
        requestDTO.setWards_province("Wards");
        requestDTO.setPayment_method(1);
        MultipartFile[] multipartFiles = new MultipartFile[0];

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        User user = new User();
        user.setUserInfor(new UserInfor());
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        when(checkConditionService.checkInputName(anyString())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.AddNewRequest(requestDTO, multipartFiles);
        });
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode());

    }

    @Test
    public void testAddNewRequest_noUserFound() {
        // Arrange
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFullname("12312");
        requestDTO.setDescription("Description");
        requestDTO.setAddress("Address");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setCity_province("City");
        requestDTO.setDistrict_province("District");
        requestDTO.setWards_province("Wards");
        requestDTO.setPayment_method(1);
        MultipartFile[] multipartFiles = new MultipartFile[0];

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername(anyString())).thenReturn(null); // No user found

        // Act & Assert
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            orderService.AddNewRequest(requestDTO, multipartFiles);
        });

        verify(orderRepository, times(0)).save(any(Orders.class));
        verify(uploadImageService, times(0)).uploadFileRequest(eq(multipartFiles), anyInt());
    }
    @Test
    public void testAddNewRequest_missingMandatoryFields() {
        // Arrange
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFullname(""); // Missing mandatory field
        requestDTO.setDescription("Description");
        requestDTO.setAddress("Address");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setCity_province("City");
        requestDTO.setDistrict_province("District");
        requestDTO.setWards_province("Wards");
        requestDTO.setPayment_method(1);
        MultipartFile[] multipartFiles = new MultipartFile[0];

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        User user = new User();
        user.setUserInfor(new UserInfor());
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        when(checkConditionService.checkInputName(anyString())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.AddNewRequest(requestDTO, multipartFiles);
        });
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode());
        verify(orderRepository, times(0)).save(any(Orders.class));
        verify(uploadImageService, times(0)).uploadFileRequest(eq(multipartFiles), anyInt());
    }

    @Test
    public void testAddNewRequest_specialConditions() {
        // Arrange
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setFullname("SpecialCaseName");
        requestDTO.setDescription("Special case description");
        requestDTO.setAddress("Special case address");
        requestDTO.setPhoneNumber("1234567890");
        requestDTO.setCity_province("SpecialCity");
        requestDTO.setDistrict_province("SpecialDistrict");
        requestDTO.setWards_province("SpecialWards");
        requestDTO.setPayment_method(2); // Some special payment method
        MultipartFile[] multipartFiles = new MultipartFile[0];

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("special_user");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        User user = new User();
        user.setUserInfor(new UserInfor());
        when(userRepository.getUserByUsername(anyString())).thenReturn(user);

        Status_Order statusOrder = new Status_Order();
        when(statusOrderRepository.findById(7)).thenReturn(statusOrder);

        Orders lastOrder = null; // Simulating no previous order
        when(orderRepository.findOrderTop(anyString())).thenReturn(lastOrder);

        when(checkConditionService.checkInputName(anyString())).thenReturn(true);

        Orders savedOrder = new Orders();
        when(orderRepository.save(any(Orders.class))).thenReturn(savedOrder);

        // Act
        Orders result = orderService.AddNewRequest(requestDTO, multipartFiles);

        // Assert
        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Orders.class));
        verify(uploadImageService, times(1)).uploadFileRequest(multipartFiles, savedOrder.getOrderId());
    }
    @Test
    public void testGetAllProductRequestByUserId_success() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setUserId(1);
        List<RequestProducts> requestProductsList = new ArrayList<>();
        requestProductsList.add(new RequestProducts());

        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(requestProductRepository.findByUserId(user.getUserId())).thenReturn(requestProductsList);

        // Act
        List<RequestProducts> result = orderService.GetAllProductRequestByUserId();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(requestProductRepository, times(1)).findByUserId(user.getUserId());
    }

    @Test
    public void testGetAllProductRequestByUserId_noProductsFound() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setUserId(1);

        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(requestProductRepository.findByUserId(user.getUserId())).thenReturn(null); // No products found

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.GetAllProductRequestByUserId();
        });
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }


    @Test
    public void testGetAllRequestByUserId_success() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setUserId(1);
        List<Orders> ordersList = new ArrayList<>();
        ordersList.add(new Orders());

        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(orderRepository.findByUserId(user.getUserId())).thenReturn(ordersList);

        // Act
        List<Orders> result = orderService.GetAllRequestByUserId();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(orderRepository, times(1)).findByUserId(user.getUserId());
    }

    @Test
    public void testGetAllRequestByUserId_noOrdersFound() {
        // Arrange
        UserDetails userDetails = mock(UserDetails.class);
        User user = new User();
        user.setUserId(1);

        when(userDetails.getUsername()).thenReturn("username");
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername(anyString())).thenReturn(user);
        when(orderRepository.findByUserId(user.getUserId())).thenReturn(null); // No orders found

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            orderService.GetAllRequestByUserId();
        });
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }
}
