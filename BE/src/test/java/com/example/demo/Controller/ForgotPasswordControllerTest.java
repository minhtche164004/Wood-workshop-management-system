package com.example.demo.Controller;

import com.example.demo.Entity.ChangePassword;
import com.example.demo.Entity.ForgotPassword;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Mail.EmailService;
import com.example.demo.Mail.MailBody;
import com.example.demo.Repository.ForgotPasswordRepository;
import com.example.demo.Repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ForgotPasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private ForgotPasswordRepository forgotPasswordRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testVerifyEmail() throws Exception {
        User user = new User();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        mockMvc.perform(post("/api/auth/forgotPassword/verifyMail/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("Check OTP đã được gửi đến gmail!"));
    }

    @Test
    void testVerifyOtp() throws Exception {
        User user = new User();
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setExpirationTime(new Date(System.currentTimeMillis() + 40 * 1000)); // Set future date
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(anyInt(), any(User.class))).thenReturn(Optional.of(forgotPassword));

        mockMvc.perform(post("/api/auth/forgotPassword/verifyOtp/123456/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("OTP verified!"));
    }

    @Test
    void testVerifyExpiredOtp() throws Exception {
        User user = new User();
        ForgotPassword forgotPassword = new ForgotPassword();
        forgotPassword.setExpirationTime(new Date(System.currentTimeMillis() - 40 * 1000)); // Set past date
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(forgotPasswordRepository.findByOtpAndUser(anyInt(), any(User.class))).thenReturn(Optional.of(forgotPassword));

        mockMvc.perform(post("/api/auth/forgotPassword/verifyOtp/123456/test@example.com"))
                .andExpect(status().isExpectationFailed())
              ;
    }

    @Test
    void testChangePassword() throws Exception {
        ChangePassword changePassword = new ChangePassword("newPassword", "newPassword");

        mockMvc.perform(post("/api/auth/forgotPassword/changePassword/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(changePassword)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password has been changed!"));
    }

    @Test
    void testChangePasswordMismatch() throws Exception {
        ChangePassword changePassword = new ChangePassword("newPassword", "differentPassword");

        mockMvc.perform(post("/api/auth/forgotPassword/changePassword/test@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(changePassword)))
                .andExpect(status().isBadRequest())
             ;
    }
}