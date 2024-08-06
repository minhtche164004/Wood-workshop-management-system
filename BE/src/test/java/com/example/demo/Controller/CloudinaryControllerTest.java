package com.example.demo.Controller;

import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CloudinaryService;
import com.example.demo.Entity.Products;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CloudinaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudinaryService cloudinaryService;

    @Test
    void testAddNewProduct() throws Exception {
        // Mocking the behavior of cloudinaryService.upload()
        Map<String, String> response = new HashMap<>();
        response.put("url", "http://example.com/image.jpg");
        when(cloudinaryService.upload(any(MultipartFile.class), anyString())).thenReturn(response);

        // Mock file to be uploaded
        MultipartFile file = Mockito.mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("thumbnail.jpg");

        mockMvc.perform(multipart("/api/auth/cloudinary/add")
                        .file("test", file.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
              ;
    }


}