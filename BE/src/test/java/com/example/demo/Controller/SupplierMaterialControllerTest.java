package com.example.demo.Controller;
import com.example.demo.Dto.SupplierDTO.SupplierMaterialDTO;
import com.example.demo.Entity.Suppliermaterial;
import com.example.demo.Service.SupplierMaterialService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierMaterialService supplierMaterialService;

    private Gson gson = new GsonBuilder().create();

    @Test
    void testGetAllSupplier() throws Exception {
        when(supplierMaterialService.GetAllSupplier()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/supplier/GetAllSupplier"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testGetSupplierById() throws Exception {
        Suppliermaterial suppliermaterial = new Suppliermaterial();
        when(supplierMaterialService.GetSuppliermaterialById(anyInt())).thenReturn(suppliermaterial);

        mockMvc.perform(get("/api/auth/supplier/GetSupplierById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testAddNewSupplier() throws Exception {
        SupplierMaterialDTO supplierMaterialDTO = new SupplierMaterialDTO();
        supplierMaterialDTO.setSupplierName("Valid Supplier Name"); // Set valid value
        supplierMaterialDTO.setPhoneNumber("1234567890"); // Set valid phone number
        supplierMaterialDTO.setSub_material_id(1); // Set valid ID or any value as needed

        Suppliermaterial suppliermaterial = new Suppliermaterial();
        when(supplierMaterialService.AddNewSupplier(any(SupplierMaterialDTO.class))).thenReturn(suppliermaterial);

        mockMvc.perform(post("/api/auth/supplier/AddNewSupplier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(supplierMaterialDTO)))
                .andExpect(status().isOk());
    }
    @Test
    void testEditSupplier() throws Exception {
        // Create and populate SupplierMaterialDTO
        SupplierMaterialDTO supplierMaterialDTO = new SupplierMaterialDTO();
        supplierMaterialDTO.setSupplierName("Updated Supplier Name"); // Set valid value
        supplierMaterialDTO.setPhoneNumber("0987654321"); // Set valid phone number
        supplierMaterialDTO.setSub_material_id(2); // Set valid ID or any value as needed

        Suppliermaterial suppliermaterial = new Suppliermaterial();
        when(supplierMaterialService.EditSupplier(anyInt(), any(SupplierMaterialDTO.class))).thenReturn(suppliermaterial);

        mockMvc.perform(put("/api/auth/supplier/EditSupplier")
                        .param("id", "1") // Set the ID for the supplier to be edited
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(supplierMaterialDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists()); // Validate the response
    }

    @Test
    void testGetAllSupplierMaterialsName() throws Exception {
        when(supplierMaterialService.GetListName()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/supplier/getAllName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testSearchByName() throws Exception {
        when(supplierMaterialService.SearchSupplierByName(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/supplier/SearchByName")
                        .param("key", "test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testDeleteSupplier() throws Exception {
        mockMvc.perform(delete("/api/auth/supplier/DeleteSupplier")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Xoá nhà cung cấp vật liệu thành công"));
    }
}