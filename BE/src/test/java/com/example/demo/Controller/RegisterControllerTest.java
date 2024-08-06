package com.example.demo.Controller;

import com.example.demo.Dto.UserDTO.RegisterDTO;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpSession;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailService emailService;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testRegisterUserAccount() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("username123");
        registerDTO.setPassword("pass1234");
        registerDTO.setCheckPass("pass1234");
        registerDTO.setEmail("test@example.com");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setAddress("123 Main St");
        registerDTO.setFullname("John Doe");
        registerDTO.setStatus(1);
        registerDTO.setBank_name("Bank Name");
        registerDTO.setBank_number("123456789");
        registerDTO.setCity("City Name");
        registerDTO.setDistrict("District Name");
        registerDTO.setWards("Ward Name");

        mockMvc.perform(post("/api/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(registerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Check Mail để kiểm tra OTP"));
    }
    @Test
    void testVerifyEmailSuccess() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("gmailregister", "test@example.com");
        session.setAttribute("otp", "123456");
        RegisterDTO registerDTO = new RegisterDTO();
        session.setAttribute("userDto", registerDTO);

        when(userService.signup(any(RegisterDTO.class))).thenReturn(null);

        mockMvc.perform(post("/api/auth/verifyMail1/123456").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Xác Thực OTP thành công, Vui Lòng quay lại Đăng Nhập"));
    }

    @Test
    void testVerifyEmailInvalidOtp() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("gmailregister", "test@example.com");
        session.setAttribute("otp", "123456");

        session.setAttribute("attempts", 1);

        doThrow(new AppException(ErrorCode.INVALID_OTP)).when(userService).checkConditions(any(RegisterDTO.class));

        mockMvc.perform(post("/api/auth/verifyMail1/654321").session(session))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testVerifyEmailTooManyAttempts() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("gmailregister", "test@example.com");
        session.setAttribute("otp", "123456");

        session.setAttribute("attempts", 2);

        doThrow(new AppException(ErrorCode.TOO_MANY_ATTEMPTS)).when(userService).checkConditions(any(RegisterDTO.class));

        mockMvc.perform(post("/api/auth/verifyMail1/654321").session(session))
                .andExpect(status().isBadRequest());
    }
}