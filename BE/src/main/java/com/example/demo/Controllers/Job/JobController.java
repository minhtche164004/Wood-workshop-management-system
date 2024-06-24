package com.example.demo.Controllers.Job;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.OrderDetailRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.JobService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/api/auth/job/")
@AllArgsConstructor
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();
    //màn hình quản lí tiến độ sản phẩm  request_product, filter cái request thì call đến api này
    @GetMapping("/getListProductRequestForJob")
    public ApiResponse<?> getListProductRequestForJob() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getListRequestProductJob());
        return apiResponse;
    }
    //lúc filter các có sẵn thì call api này
    @GetMapping("/getListProductForJob")
    public ApiResponse<?> getListProductForJob() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getListProductJob());
        return apiResponse;
    }

    @GetMapping("/getRequestProductInOrderDetailByCode")
    public ApiResponse<?> getRequestProductInOrderDetailByCode(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getRequestProductInOrderDetailByCode(key));
        return apiResponse;
    }
    @GetMapping("/getListProductJobByNameOrCode")
    public ApiResponse<?> getListProductJobByNameOrCode(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getListProductJobByNameOrCode(key));
        return apiResponse;
    }

    @GetMapping("/findUsersWithPosition1AndLessThan3Jobs")
    public ApiResponse<?> findUsersWithPosition1AndLessThan3Jobs() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.findUsersWithPosition1AndLessThan3Jobs());
        return apiResponse;
    }

    @GetMapping("/findUsersWithPosition2AndLessThan3Jobs")
    public ApiResponse<?> findUsersWithPosition2AndLessThan3Jobs() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.findUsersWithPosition2AndLessThan3Jobs());
        return apiResponse;
    }

    @GetMapping("/findUsersWithPosition3AndLessThan3Jobs")
    public ApiResponse<?> findUsersWithPosition3AndLessThan3Jobs() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.findUsersWithPosition3AndLessThan3Jobs());
        return apiResponse;
    }

    @PostMapping("/CreateJobs")
    public ApiResponse<?> CreateJobs(@RequestBody JobDTO jobDTO , @RequestParam("user_id") int user_id, @RequestParam("p_id") int p_id,@RequestParam("status_id") int status_id) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.CreateJob(jobDTO,user_id,p_id,status_id));
        return apiResponse;
    }

}
