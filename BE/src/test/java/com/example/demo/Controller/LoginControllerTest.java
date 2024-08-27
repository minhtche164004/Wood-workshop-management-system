//package com.example.demo.Controller;
//import com.example.demo.Jwt.JwtAuthenticationResponse;
//import com.example.demo.Jwt.RefreshTokenRequest;
//import com.example.demo.Request.LoginRequest;
//import com.example.demo.Service.UserService;
//import com.example.demo.Response.ApiResponse;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import redis.clients.jedis.JedisPooled;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class LoginControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private JedisPooled jedis;
//
//    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//
//    @Test
//    void testLogin() throws Exception {
//        LoginRequest loginRequest = new LoginRequest();
//        loginRequest.setUsername("testUser");
//        loginRequest.setPassword("testPassword");
//
//        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
//
//        when(userService.signin(any(LoginRequest.class))).thenReturn(jwtResponse);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(gson.toJson(loginRequest)))
//                .andExpect(status().isOk())
//             ;
//    }
//
//    @Test
//    void testRefreshToken() throws Exception {
//        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
//
//        JwtAuthenticationResponse jwtResponse = new JwtAuthenticationResponse();
//
//        when(userService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(jwtResponse);
//
//        mockMvc.perform(post("/api/auth/refreshToken")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(gson.toJson(refreshTokenRequest)))
//                .andExpect(status().isOk())
//                ;
//    }
//}