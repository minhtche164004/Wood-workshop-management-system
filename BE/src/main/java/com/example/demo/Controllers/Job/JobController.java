package com.example.demo.Controllers.Job;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Repository.OrderDetailRepository;
import com.example.demo.Repository.ProcessproducterrorRepository;
import com.example.demo.Repository.Status_Job_Repository;
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
    @Autowired
    private Status_Job_Repository status_Job_Repository;
    @Autowired
    private ProcessproducterrorRepository processproducterrorRepository;

    //màn hình quản lí tiến độ sản phẩm  request_product, filter cái request thì call đến api này
    @GetMapping("/getListProductRequestForJob")
    public ApiResponse<?> getListProductRequestForJob() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_request_job";
////        jedis.del(cacheKey);
//        List<JobProductDTO> jobProductDTOS;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<JobProductDTO>>() {
//            }.getType();
//
//            jobProductDTOS = gson.fromJson(cachedData, type);
//        } else {
//            jobProductDTOS = jobService.getListRequestProductJob();
//            String jsonData = gson.toJson(jobProductDTOS);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
        apiResponse.setResult(jobService.getListRequestProductJob());
        return apiResponse;
    }
    @GetMapping("/getJobById")
    public ApiResponse<?> getJobById(@RequestParam("job_id") int job_id) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.GetJobById(job_id));
        return apiResponse;
    }

    @GetMapping("/countCompletedJobsByMonthAndYear")
    public ApiResponse<?> countCompletedJobsByMonthAndYear(@RequestParam("status_name") String status_name,@RequestParam("month")int month,
                                                           @RequestParam("year") int year) {
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.CountQuantityOfJob(status_name,month,year));
        return apiResponse;
    }
//    //lúc filter các có sẵn thì call api này
    @GetMapping("/getListProductForJob")
    public ApiResponse<?> getListProductForJob() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_job";
//
////        jedis.del(cacheKey);
//        List<JobProductDTO> jobProductDTOS;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<JobProductDTO>>() {
//            }.getType();
//
//            jobProductDTOS = gson.fromJson(cachedData, type);
//        } else {
//            jobProductDTOS = jobService.getListProductJob();
//            String jsonData = gson.toJson(jobProductDTOS);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
        apiResponse.setResult(jobService.getListProductJob());
        return apiResponse;
    }
//
    @GetMapping("/getRequestProductInOrderDetailByCode")
    public ApiResponse<?> getRequestProductInOrderDetailByCode(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_request_products_inOrder_ByCode";
//        List<JobProductDTO> jobProductDTOS;
//        String cachedData = jedis.hget(cacheKey,key);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<JobProductDTO>>() {
//            }.getType();
//
//            jobProductDTOS = gson.fromJson(cachedData, type);
//        } else {
//            jobProductDTOS = jobService.getRequestProductInOrderDetailByCode(key);
//            String jsonData = gson.toJson(jobProductDTOS);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
        apiResponse.setResult(jobService.getRequestProductInOrderDetailByCode(key));
        return apiResponse;

    }
    @GetMapping("/getListProductJobByNameOrCodeProduct")
    public ApiResponse<?> getListProductJobByNameOrCode(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_products_job_byName_Code";
//        List<JobProductDTO> jobProductDTOS;
//        String cachedData = jedis.hget(cacheKey,key);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<JobProductDTO>>() {
//            }.getType();
//
//            jobProductDTOS = gson.fromJson(cachedData, type);
//        } else {
//            jobProductDTOS = jobService.getListProductJobByNameOrCode(key);
//            String jsonData = gson.toJson(jobProductDTOS);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
        apiResponse.setResult(jobService.getListProductJobByNameOrCode(key));
        return apiResponse;

    }

//    @GetMapping("/findUsersWithPosition1AndLessThan3Jobs")
//    public ApiResponse<?> findUsersWithPosition1AndLessThan3Jobs() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(jobRepository.findUsersWithPositionAndLessThan3Jobs());
//        return apiResponse;
//    }
//
//    @GetMapping("/findUsersWithPosition2AndLessThan3Jobs")
//    public ApiResponse<?> findUsersWithPosition2AndLessThan3Jobs() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(jobRepository.findUsersWithPositionAndLessThan3Jobs());
//        return apiResponse;
//    }

    //có 3 loại type là thợ mộc 1, thợ sơn 2, thợ đánh nhám 3
    @GetMapping("/findUsersWithPositionAndLessThan3Jobs")
    public ApiResponse<?> findUsersWithPosition3AndLessThan3Jobs(@RequestParam("type") int type_) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.findUsersWithPositionAndLessThan3Jobs(type_));
        return apiResponse;

    }
    @GetMapping("/getListStatusJobByType")
    public ApiResponse<?> getListStatusJobByType(@RequestParam("type") int type_) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.findByStatusByType(type_));
        return apiResponse;
    }
    @GetMapping("/countJobsByUserId")
    public ApiResponse<?> countJobsByUserId(@RequestParam("user_id") int user_id) {
        ApiResponse<Integer> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobRepository.countJobsByUserId(user_id));
        return apiResponse;
    }

    @PostMapping("/CreateJobs")
    public ApiResponse<?> CreateJobs(@RequestBody JobDTO jobDTO , @RequestParam("user_id") int user_id, @RequestParam("p_id") int p_id, @RequestParam("status_id") int status_id, @RequestParam("job_id") int job_id,@RequestParam("type_job") int type_job) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        jedis.del("all_products_job");
        jedis.del("all_products_request_job");
        apiResponse.setResult(jobService.CreateJob(jobDTO,user_id,p_id,status_id,job_id,type_job));
        return apiResponse;
    }

    @PostMapping("/CreateProductForJobs")
    public ApiResponse<?> CreateProductForJobs(@RequestParam("p_id") int p_id, @RequestParam("quantity") int quantity) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.AddProductForJob(p_id,quantity));
        return apiResponse;
    }

    @PutMapping("/acceptJob")
    public ApiResponse<?> AcceptJob(@RequestParam("job_id") int job_id,@RequestParam("status_id") int status_id) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        jedis.del("all_products_job");
        jedis.del("all_products_request_job");
        apiResponse.setResult(jobService.CreateJob_Log(job_id, status_id));
        return apiResponse;

    }

    @PutMapping("/EditJob")
    public ApiResponse<?> EditJob(@RequestParam("job_id") int job_id,@RequestBody JobDTO jobDTO) {
        ApiResponse<Jobs> apiResponse = new ApiResponse<>();
        jedis.del("all_products_job");
        jedis.del("all_products_request_job");
        apiResponse.setResult(jobService.EditJobs(jobDTO,job_id));
        return apiResponse;

    }

    @GetMapping("/getListJobWasDone")
    public ApiResponse<?> getJobWasDone() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_job_was_done";
//        jedis.del(cacheKey);
        List<JobDoneDTO> jobsList;
        String cachedData = jedis.get(cacheKey);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<JobDoneDTO>>() {
            }.getType();

            jobsList = gson.fromJson(cachedData, type);
        } else {
            jobsList = jobRepository.findAllJobForEmployeeDone();
            String jsonData = gson.toJson(jobsList);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1200);
        }
        apiResponse.setResult(jobsList);
        return apiResponse;
    }

    @GetMapping("/findAllJobForDoneByEmployeeID")
    public ApiResponse<?> findAllJobForDoneByEmployeeID() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.findAllJobForDoneByEmployeeID());
        return apiResponse;
    }

//    @GetMapping("/getListJobWasDone")
//    public ApiResponse<?> getJobWasDone() {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
////        String cacheKey = "all_job_was_done";
////        jedis.del(cacheKey);
////        List<Jobs> jobsList;
////        String cachedData = jedis.get(cacheKey);
////        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
////        if (cachedData != null) {
////            Type type = new TypeToken<List<Status_Job>>() {
////            }.getType();
////
////            jobsList = gson.fromJson(cachedData, type);
////        } else {
////            jobsList = jobService.getJobWasDone();
////            String jsonData = gson.toJson(jobsList);
////            jedis.set(cacheKey, jsonData);
////            jedis.expire(cacheKey, 1200);
////        }
//        apiResponse.setResult(jobService.getJobWasDone());
//        return apiResponse;
//    }
    @GetMapping("/filterJobWasDoneByEmployeeName")
    public ApiResponse<?> filterJobWasDoneByEmployeeName(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_job_was_done_by_Name_Code";
        List<JobDoneDTO> jobsList;
        String cachedData = jedis.hget(cacheKey,key);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<JobDoneDTO>>() {
            }.getType();

            jobsList = gson.fromJson(cachedData, type);
        } else {
            jobsList = jobService.filterJobWasDoneByEmployeeName(key);
            String jsonData = gson.toJson(jobsList);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1200);
        }
        apiResponse.setResult(jobsList);
        return apiResponse;
    }

    @GetMapping("/getAllStatusJob")
    public ApiResponse<?> getAllStatusJob() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_job_was_done_by_Name_Code";
        List<Status_Job> jobsList;
        String cachedData = jedis.get(cacheKey);
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<List<Status_Job>>() {
            }.getType();

            jobsList = gson.fromJson(cachedData, type);
        } else {
            jobsList = status_Job_Repository.getAllStatus();
            String jsonData = gson.toJson(jobsList);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 1200);
        }
        apiResponse.setResult(jobsList);
        return apiResponse;
    }

    @PostMapping("/CreateProductError")
    public ApiResponse<?> CreateProductError(@RequestParam("job_id") int job_id, @RequestBody ProductErrorDTO productErrorDTO) {
        ApiResponse<Processproducterror> apiResponse = new ApiResponse<>();
       // jedis.del("all_product_error");
        apiResponse.setResult(jobService.AddProductError(job_id,productErrorDTO));
        return apiResponse;
    }
    @PutMapping("/EditProductError")
    public ApiResponse<?> EditProductError(@RequestParam("error_id") int error_id,@RequestBody ProductErrorDTO productErrorDTO) {
        ApiResponse<ProductErrorAllDTO> apiResponse = new ApiResponse<>();
       // jedis.del("all_product_error");
        apiResponse.setResult(jobService.EditProductError(error_id,productErrorDTO));
        return apiResponse;

    }
    @GetMapping("/getAllProductError")
    public ApiResponse<?> getAllProductError() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
//        String cacheKey = "all_product_error";
//        List<ProductErrorAllDTO> jobsList;
//        String cachedData = jedis.get(cacheKey);
//        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
//        if (cachedData != null) {
//            Type type = new TypeToken<List<Processproducterror>>() {
//            }.getType();
//
//            jobsList = gson.fromJson(cachedData, type);
//        } else {
//            jobsList = jobService.getAllProductError();
//            String jsonData = gson.toJson(jobsList);
//            jedis.set(cacheKey, jsonData);
//            jedis.expire(cacheKey, 1200);
//        }
        apiResponse.setResult(jobService.getAllProductError());
        return apiResponse;
    }

    @DeleteMapping("/DeleteProductError")
    public ApiResponse<?> DeleteProductError(@RequestParam("id") int id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        processproducterrorRepository.delete(processproducterrorRepository.FindByIdProductErrorId(id));
        apiResponse.setResult("Xoá sản phẩm khỏi danh sách sản phẩm lỗi thành công");
        return apiResponse;
    }

    @GetMapping("/getAllProductErrorDetail")
    public ApiResponse<?> getAllProductErrorDetail(@RequestParam("id") int id) {
        ApiResponse<ProductErrorAllDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(jobService.getProductErrorDetailById(id));
        return apiResponse;
    }
}
