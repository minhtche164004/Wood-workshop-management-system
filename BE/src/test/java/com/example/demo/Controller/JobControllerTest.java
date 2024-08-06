package com.example.demo.Controller;
import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Entity.Jobs;
import com.example.demo.Service.JobService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import redis.clients.jedis.JedisPooled;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobService jobService;

    @MockBean
    private JedisPooled jedis;

    private Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();

    @Test
    void testGetListProductRequestForJob() throws Exception {
        when(jobService.getListRequestProductJob()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/job/getListProductRequestForJob"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testGetJobById() throws Exception {
        Jobs job = new Jobs();
        when(jobService.GetJobById(anyInt())).thenReturn(job);

        mockMvc.perform(get("/api/auth/job/getJobById")
                        .param("job_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testCheckErrorOfJobHaveFixDoneOrNot() throws Exception {
        when(jobService.checkErrorOfJobHaveFixDoneOrNot(anyInt())).thenReturn(true);

        mockMvc.perform(get("/api/auth/job/checkErrorOfJobHaveFixDoneOrNot")
                        .param("job_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Test
    void testMultiFilterProductError() throws Exception {
        when(jobService.MultiFilterErrorProduct(any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/auth/job/MultiFilterProductError")
                        .param("search", "test")
                        .param("is_fix", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testCountCompletedJobsByMonthAndYear() throws Exception {
        when(jobService.CountQuantityOfJob(any(), anyInt(), anyInt())).thenReturn(10L);

        mockMvc.perform(get("/api/auth/job/countCompletedJobsByMonthAndYear")
                        .param("status_name", "completed")
                        .param("month", "7")
                        .param("year", "2023"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(10L));
    }

    @Test
    void testCreateJobs() throws Exception {
        JobDTO jobDTO = new JobDTO();
        Jobs job = new Jobs();
        when(jobService.CreateJob(any(JobDTO.class), anyInt(), anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(job);

        mockMvc.perform(post("/api/auth/job/CreateJobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(jobDTO))
                        .param("user_id", "1")
                        .param("p_id", "1")
                        .param("status_id", "1")
                        .param("job_id", "1")
                        .param("type_job", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testEmployeeSick() throws Exception {
        Jobs job = new Jobs();
        when(jobService.EmployeeSick(anyInt(), anyInt(), any(BigDecimal.class))).thenReturn(job);

        mockMvc.perform(post("/api/auth/job/EmployeeSick")
                        .param("user_id", "1")
                        .param("job_id", "1")
                        .param("cost_employee", "100.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testCreateProductForJobs() throws Exception {
        Jobs job = new Jobs();
        when(jobService.AddProductForJob(anyInt(), anyInt())).thenReturn(job);

        mockMvc.perform(post("/api/auth/job/CreateProductForJobs")
                        .param("p_id", "1")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testAcceptJob() throws Exception {
        Jobs job = new Jobs();
        when(jobService.CreateJob_Log(anyInt(), anyInt())).thenReturn(job);

        mockMvc.perform(put("/api/auth/job/acceptJob")
                        .param("job_id", "1")
                        .param("status_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }

    @Test
    void testEditJob() throws Exception {
        JobDTO jobDTO = new JobDTO();
        Jobs job = new Jobs();
        when(jobService.EditJobs(any(JobDTO.class), anyInt())).thenReturn(job);

        mockMvc.perform(put("/api/auth/job/EditJob")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(jobDTO))
                        .param("job_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists());
    }


}
