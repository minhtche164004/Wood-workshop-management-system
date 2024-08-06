package com.example.demo.Controller;

import com.example.demo.Dto.OrderDTO.DateDTO;
import com.example.demo.Entity.Advancesalary;
import com.example.demo.Service.JobService;
import com.example.demo.Response.ApiResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    @Test
    void testGetAllSalary() throws Exception {
        List<Advancesalary> salaries = Collections.emptyList();
        when(jobService.getAllAdvancesalary()).thenReturn(salaries);

        mockMvc.perform(get("/api/auth/salary/getAllSalary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @Test
    void testMultiFilterSalary() throws Exception {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(new Date());
        dateDTO.setEndDate(new Date());

        List<Advancesalary> filteredSalaries = Collections.emptyList();
        when(jobService.MultiFilterOrderSpecialOrder(any(), anyInt(), any(), any())).thenReturn(filteredSalaries);

        mockMvc.perform(post("/api/auth/salary/MultiFilterSalary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dateDTO))
                        .param("search", "test")
                        .param("isAdvanceSuccess", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @Test
    void testGetSalaryByEmployeeID() throws Exception {
        List<Advancesalary> salaries = Collections.emptyList();
        when(jobService.getAdvancesalaryByEmployeeId()).thenReturn(salaries);

        mockMvc.perform(get("/api/auth/salary/getSalaryByEmployeeID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isEmpty());
    }

    @Test
    void testUpdateBanking() throws Exception {
        Advancesalary updatedSalary = new Advancesalary();
        when(jobService.ChangeStatus(anyInt(), anyBoolean())).thenReturn(updatedSalary);

        mockMvc.perform(put("/api/auth/salary/updatebanking")
                        .param("id", "1")
                        .param("is_advance_success", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testGetMultiFilterSalary() throws Exception {
        DateDTO dateDTO = new DateDTO();
        dateDTO.setStartDate(new Date());
        dateDTO.setEndDate(new Date());

        List<Advancesalary> filteredSalaries = Collections.emptyList();
        when(jobService.multi_filter_salary(any(), any(), any(), any(), any(), any())).thenReturn(filteredSalaries);

        mockMvc.perform(post("/api/auth/salary/getMultiFillterSalary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dateDTO))
                        .param("username", "testUser")
                        .param("isAdvanceSuccess", "1")
                        .param("position_id", "1")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").isEmpty());
    }
}