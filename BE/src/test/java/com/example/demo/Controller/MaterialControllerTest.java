package com.example.demo.Controller;

import com.example.demo.Controllers.Material.MaterialController;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Service.MaterialService;
import com.example.demo.Repository.MaterialRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialService materialService;

    @Autowired
    private MaterialRepository materialRepository;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMaterials() throws Exception {
        List<Materials> materialsList = Arrays.asList(
                new Materials("1", "Material1"),
                new Materials("2", "Material2")
        );
        when(materialService.getAllMaterials()).thenReturn(materialsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.result.length()").value(materialsList.size()));
    }

    @Test
    void testGetMaterialById() throws Exception {
        Materials material = new Materials("1", "Material1");
        when(materialService.GetMaterialById(anyInt())).thenReturn(material);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/auth/GetMaterialById")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
               ;
    }

    @Test
    void testAddNewMaterial() throws Exception {
        MaterialDTO materialDTO = new MaterialDTO("1", "Material1");
        Materials material = new Materials("1", "Material1");
        when(materialService.AddNewMaterial(any(MaterialDTO.class))).thenReturn(material);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/addNewMaterial")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
               ;
    }


    @Test
    void testEditMaterial() throws Exception {
        MaterialDTO materialDTO = new MaterialDTO("1", "UpdatedMaterial");
        Materials updatedMaterial = new Materials("1", "UpdatedMaterial");
        when(materialService.EditMaterial(anyInt(), any(MaterialDTO.class))).thenReturn(updatedMaterial);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/auth/EditMaterial")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(materialDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
               ;
    }
}
