package com.example.demo.Controller;

import com.example.demo.Dto.OrderDTO.DateDTO;
import com.example.demo.Dto.ProductDTO.CreateExportMaterialProductRequest;
import com.example.demo.Dto.ProductDTO.QuantityTotalDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Dto.SubMaterialDTO.UpdateSubDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.JobService;
import com.example.demo.Service.SubMaterialService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import redis.clients.jedis.JedisPooled;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SubMaterialControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubMaterialService subMaterialService;

    @MockBean
    private ResourceLoader resourceLoader;

    @MockBean
    private JobService jobService;

    @MockBean
    private JedisPooled jedis;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testGetAllSubMaterials() throws Exception {
        when(subMaterialService.getAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/getall"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testMultiFilterInputSubMaterial() throws Exception {
        DateDTO dateDTO = new DateDTO();
        when(subMaterialService.MultiFilterInputSubMaterial(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(post("/api/auth/submaterial/MultiFilterInputSubMaterial")
                        .param("search", "test")
                        .param("materialId", "1")
                        .param("action_type_id", "1")
                        .param("minPrice", "100.0")
                        .param("maxPrice", "200.0")
                        .param("sortDirection", "asc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testGetAllInputSubMaterial() throws Exception {
        when(subMaterialService.getAllInputSubMaterial()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/getAllInputSubMaterial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testGetSubmaterialById() throws Exception {
        SubMaterialViewDTO subMaterialViewDTO = new SubMaterialViewDTO();
        when(subMaterialService.getSubMaterialById(anyInt())).thenReturn(subMaterialViewDTO);

        mockMvc.perform(get("/api/auth/submaterial/getSubmaterialById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testEditSubMaterial() throws Exception {
        SubMaterialViewDTO subMaterialViewDTO = new SubMaterialViewDTO();
        when(subMaterialService.EditSubMaterial(anyInt(), any(SubMaterialViewDTO.class))).thenReturn(subMaterialViewDTO);

        mockMvc.perform(put("/api/auth/submaterial/editSubMaterial")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(subMaterialViewDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }



    @Test
    void testGetAllSubMaterialsName() throws Exception {
        when(subMaterialService.GetListName()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/getAllName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testFilterByMaterial() throws Exception {
        when(subMaterialService.FilterByMaterial(anyInt())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/FilterByMaterial")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }



    @Test
    void testGetAllMaterialForEmployee() throws Exception {
        when(jobService.getAllMaterialForEmployee()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/GetAllMaterialForEmployee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testSearchByNameOrCode() throws Exception {
        when(subMaterialService.SearchByNameorCode(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/submaterial/SearchByNameorCode")
                        .param("key", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }


    @Test
    void testUploadCustomersData() throws Exception {
        when(subMaterialService.saveSubMaterialToDatabase(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(multipart("/api/auth/submaterial/upload-submaterial-data")
                        .file("file", "content".getBytes()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testDownloadFile() throws Exception {
        Resource resource = Mockito.mock(Resource.class);
        when(resource.exists()).thenReturn(true);
        when(resourceLoader.getResource(anyString())).thenReturn(resource);

        mockMvc.perform(get("/api/auth/submaterial/download-form-submaterial-data-excel"))
                .andExpect(status().isOk());
    }



}