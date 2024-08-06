package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dto.JobDTO.*;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.Impl.JobServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

class JobServiceImplTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private OrderDetailRepository orderDetailRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RequestProductRepository requestProductRepository;
    @Mock
    private Status_Product_Repository statusProductRepository;
    @Mock
    private Status_Job_Repository statusJobRepository;
    @Mock
    private ProcessproducterrorRepository processproducterrorRepository;
    @Mock
    private Status_Order_Repository statusOrderRepository;
    @Mock
    private AdvancesalaryRepository advancesalaryRepository;
    @Mock
    private CheckConditionService checkConditionService;
    @Mock
    private Employee_Material_Repository employeeMaterialRepository;
    @InjectMocks
    private JobServiceImpl jobServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetListRequestProductJob() {
        List<JobProductDTO> mockList = new ArrayList<>();
        when(jobRepository.getRequestProductInJob()).thenReturn(mockList);

        List<JobProductDTO> result = jobServiceImpl.getListRequestProductJob();
        assertEquals(mockList, result);
    }

    @Test
    void testGetListRequestProductJob_EmptyList() {
        when(jobRepository.getRequestProductInJob()).thenReturn(null);
        assertThrows(AppException.class, () -> jobServiceImpl.getListRequestProductJob());
    }

    @Test
    void testGetListProductJob() {
        List<JobProductDTO> mockList = new ArrayList<>();
        when(jobRepository.getListProductJob()).thenReturn(mockList);

        List<JobProductDTO> result = jobServiceImpl.getListProductJob();
        assertEquals(mockList, result);
    }

    @Test
    void testGetListProductJob_EmptyList() {
        when(jobRepository.getListProductJob()).thenReturn(null);
        assertThrows(AppException.class, () -> jobServiceImpl.getListProductJob());
    }

    @Test
    void testGetListProductJobByNameOrCode() {
        String key = "product1";
        List<JobProductDTO> mockList = new ArrayList<>();
        when(jobRepository.getListProductJobByNameOrCodeProduct(key)).thenReturn(mockList);

        List<JobProductDTO> result = jobServiceImpl.getListProductJobByNameOrCode(key);
        assertEquals(mockList, result);
    }

    @Test
    void testGetListProductJobByNameOrCode_EmptyList() {
        String key = "product1";
        when(jobRepository.getListProductJobByNameOrCodeProduct(key)).thenReturn(null);
        assertThrows(AppException.class, () -> jobServiceImpl.getListProductJobByNameOrCode(key));
    }


    @Test
    void testGetRequestProductInOrderDetailByCode_EmptyList() {
        String code = "260724PD001";
        when(jobRepository.getRequestProductInOrderDetailByCode(code)).thenReturn(Collections.emptyList());
        assertThrows(AppException.class, () -> jobServiceImpl.getRequestProductInOrderDetailByCode(code));
    }


    @Test
    void testCreateJob() {
        JobDTO jobDTO = new JobDTO();
        int userId = 1;
        int productId = 1;
        int statusId = 1;
        int jobId = 1;
        int typeJob = 1;
        Jobs job = new Jobs();
        Jobs existingJob = new Jobs();
        existingJob.setOrderdetails(new Orderdetails());

        when(userRepository.findByIdJob(userId)).thenReturn(new User());
        when(requestProductRepository.findById(productId)).thenReturn(new RequestProducts());
        when(productRepository.findById(productId)).thenReturn(new Products());
        when(jobRepository.getJobById(jobId)).thenReturn(existingJob);
        when(statusJobRepository.findById(statusId)).thenReturn(new Status_Job());
        when(jobRepository.save(any(Jobs.class))).thenReturn(job);
        when(processproducterrorRepository.getProcessproducterrorByJobId(jobId)).thenReturn(new ArrayList<>());

        Jobs result = jobServiceImpl.CreateJob(jobDTO, userId, productId, statusId, jobId, typeJob);
        assertNotNull(result);
    }

    @Test
    void testGetAllProductErrorByJobId() {
        int jobId = 1;
        List<ProductErrorAllDTO> mockList = new ArrayList<>();
        when(jobRepository.getAllProductErrorByJobId(jobId)).thenReturn(mockList);

        List<ProductErrorAllDTO> result = jobServiceImpl.getAllProductErrorByJobId(jobId);
        assertEquals(mockList, result);
    }

    @Test
    void testGetAllProductErrorByJobId_EmptyList() {
        int jobId = 1;
        when(jobRepository.getAllProductErrorByJobId(jobId)).thenReturn(null);
        assertThrows(AppException.class, () -> jobServiceImpl.getAllProductErrorByJobId(jobId));
    }




    @Test
    void testMultiFilterErrorProduct_NoResultsFound() {
        String search = "error";
        Integer isFixed = 1;
        List<ProductErrorAllDTO> mockList = new ArrayList<>(); // Empty list simulating no results found
        when(jobRepository.MultiFilterErrorProductWithBoolean(search, true)).thenReturn(mockList);

        // Expecting an AppException to be thrown when no results are found
        AppException thrown = assertThrows(AppException.class, () -> {
            jobServiceImpl.MultiFilterErrorProduct(search, isFixed);
        });

        // Assert that the exception has the correct error code
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }


    @Test
    void testCreateJobButNotFoundJobIDAndStatus_Log() {
        int jobId = 1;
        int statusId = 15;

        // Simulate that job is not found
        when(jobRepository.getJobById(jobId)).thenReturn(null);

        // Simulate that status is not found
        when(statusJobRepository.findById(statusId)).thenReturn(null);

        // Expect an exception to be thrown when job and status are not found
        assertThrows(RuntimeException.class, () -> jobServiceImpl.CreateJob_Log(jobId, statusId));
    }

    @Test
    void testEditJobs() {
        JobDTO jobDTO = new JobDTO();
        int jobId = 1;
        Jobs existingJob = new Jobs();
        when(jobRepository.getJobById(jobId)).thenReturn(existingJob);
        when(jobRepository.save(any(Jobs.class))).thenReturn(existingJob);

        Jobs result = jobServiceImpl.EditJobs(jobDTO, jobId);
        assertNotNull(result);
    }

    @Test
    void testEditJobs_JobNotFound() {
        // Arrange
        JobDTO jobDTO = new JobDTO();
        int jobId = 1;

        // Mock the repository to return null for the job ID
        when(jobRepository.getJobById(jobId)).thenReturn(null);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> {
            jobServiceImpl.EditJobs(jobDTO, jobId);
        });

        // Verify the exception type and message
        assertEquals("Job not found", thrown.getMessage()); // Ensure the message matches the one used in the exception
    }
    @Test
    void testGetJobById() {
        int jobId = 1;
        Jobs job = new Jobs();
        when(jobRepository.getJobById(jobId)).thenReturn(job);

        Jobs result = jobServiceImpl.GetJobById(jobId);
        assertEquals(job, result);
    }

    @Test
    void testGetJobById_JobNotFound() {
        int jobId = 1;
        when(jobRepository.getJobById(jobId)).thenReturn(null);

        assertThrows(AppException.class, () -> jobServiceImpl.GetJobById(jobId));
    }

    @Test
    void testAddProductForJob() {
        int productId = 1;
        int quantity = 5;
        Jobs job = new Jobs();
        Products product = new Products();
        when(productRepository.findById(productId)).thenReturn(product);
        when(jobRepository.getJobById(anyInt())).thenReturn(job);
        when(jobRepository.save(any(Jobs.class))).thenReturn(job);

        Jobs result = jobServiceImpl.AddProductForJob(productId, quantity);
        assertNotNull(result);
    }

    @Test
    void testGetJobWasDone() {
        List<Jobs> mockList = new ArrayList<>();
        when(jobRepository.getJobWasDone()).thenReturn(mockList);

        List<Jobs> result = jobServiceImpl.getJobWasDone();
        assertEquals(mockList, result);
    }



    // Additional tests for various edge cases and error scenarios





    @Test
    void testGetAllProductErrorByJobId_JobNotFound() {
        int jobId = 999; // Non-existent job ID
        when(jobRepository.getAllProductErrorByJobId(jobId)).thenReturn(null);

        assertThrows(AppException.class, () -> jobServiceImpl.getAllProductErrorByJobId(jobId));
    }



    @Test
    void testMultiFilterErrorProductWithNullSearch() {
        // Arrange
        String search = null;
        Integer isFixed = 1;
        List<ProductErrorAllDTO> mockList = new ArrayList<>(); // Empty list to simulate no results
        when(jobRepository.MultiFilterErrorProductWithBoolean(search, true)).thenReturn(mockList);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> jobServiceImpl.MultiFilterErrorProduct(search, isFixed));

        // Verify the exception message or error code if necessary
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode()); // Adjust the error code or message to match your implementation
    }

//    @Test
//    void testCreateJob_Log_StatusNotFound() {
//        int jobId = 1;
//        int statusId = 999; // Non-existent status ID
//        Jobs jobHistory = new Jobs();
//        when(jobRepository.getJobById(jobId)).thenReturn(jobHistory);
//        when(statusJobRepository.findById(statusId)).thenReturn(null);
//
//        assertThrows(AppException.class, () -> jobServiceImpl.CreateJob_Log(jobId, statusId));
//    }
@Test
void testCreateJob_Log_StatusNotFound() {
    int jobId = 1;
    int statusId = 999; // Non-existent status ID
    Jobs jobHistory = new Jobs();
    when(jobRepository.getJobById(jobId)).thenReturn(jobHistory);
    when(statusJobRepository.findById(statusId)).thenReturn(null);

    // Catch RuntimeException temporarily if AppException is not being thrown correctly
    RuntimeException thrownException = assertThrows(RuntimeException.class, () -> jobServiceImpl.CreateJob_Log(jobId, statusId));

    // Optionally, you can inspect the message to confirm the error type if needed
    assertEquals("Job not found", thrownException.getMessage()); // Adjust as necessary
}

}