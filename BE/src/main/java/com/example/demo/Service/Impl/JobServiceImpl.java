package com.example.demo.Service.Impl;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private Status_Product_Repository statusProductRepository;
    @Autowired
    private Status_Job_Repository statusJobRepository;



    @Override
    public List<JobProductDTO> getListRequestProductJob() {
List<JobProductDTO> orderdetailsList = jobRepository.getRequestProductInJob();
        return orderdetailsList;
    }

    @Override
    public List<JobProductDTO> getListProductJob() {
        List<JobProductDTO> orderdetailsList = jobRepository.getListProductJob();
        return orderdetailsList;
    }

    @Override
    public List<JobProductDTO> getListProductJobByNameOrCode(String key) {
        List<JobProductDTO> orderdetailsList = jobRepository.getListProductJobByNameOrCodeProduct(key);
        return orderdetailsList;
    }

    @Override
    public List<JobProductDTO> getRequestProductInOrderDetailByCode(String code) {
        List<JobProductDTO> jobProductDTOS = jobRepository.getRequestProductInOrderDetailByCode(code);
        if(jobProductDTOS.isEmpty()){
            throw  new AppException(ErrorCode.NOT_FOUND);
        }
        return jobProductDTOS;
    }

    @Override
    public Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id,int status_id,int job_id) {
        Jobs jobs = new Jobs();
        User user = userRepository.findByIdJob(user_id);
        jobs.setUser(user);
        Products products =productRepository.findById(p_id);
        RequestProducts requestProducts = requestProductRepository.findById(p_id);

        if(products == null){ //tức là đang phân job cho requets product
            jobs.setRequestProducts(requestProducts);
            jobs.setProduct(null);
        }else {////tức là đang phân job cho  product có sẵn
            jobs.setProduct(products);
            jobs.setRequestProducts(null);
        }
        jobs.setDescription(jobDTO.getDescription());
        jobs.setQuantityProduct(jobDTO.getQuantity_product());
        jobs.setCost(jobDTO.getCost());
        jobs.setJob_name(jobDTO.getJob_name());
        jobs.setTimeFinish(jobDTO.getFinish());
        Jobs jobs_order_detail = jobRepository.getJobById(job_id);
        jobs.setOrderdetails(jobs_order_detail.getOrderdetails());
        jobs.setTimeStart(jobDTO.getStart());
        Status_Job statusJob = statusJobRepository.findById(1); //set mac dinh la job dang thi cong
        jobs.setStatus(statusJob);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        Jobs lastJob = jobRepository.findJobsTop(dateString + "JB");
        int count = lastJob != null ? Integer.parseInt(lastJob.getCode().substring(8)) + 1 : 1;
        String code = dateString + "JB" + String.format("%03d", count);
        jobs.setCode(code);
        jobs.setJob_log(false);
        jobRepository.save(jobs);
        //nếu là giao việc thành công thì sẽ chuyển trạng thái của sản phẩm
        if(status_id == 3 && products ==null){//chưa giao việc  -> phân đang làm mộc
            requestProducts.setStatus(statusProductRepository.findById(4));
            requestProductRepository.save(requestProducts);
        }
        if(status_id == 3 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(4));
            productRepository.save(products);
        }

        if(status_id == 6 && products ==null){// đã làm mộc xong ->  phân đang đánh nhám(tức là công đoạn đang làm mộc đã xong)
            requestProducts.setStatus(statusProductRepository.findById(7));
            requestProductRepository.save(requestProducts);
        }
        if(status_id == 6 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(7));
            productRepository.save(products);
        }

        if(status_id == 9 && products ==null){//đã đánh nhám xong -> phân đang sơn(tức là công đoạn đang đánh nhám đã xong)
            requestProducts.setStatus(statusProductRepository.findById(10));
            requestProductRepository.save(requestProducts);
        }
        if(status_id == 9 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(10));
            productRepository.save(products);
        }
        return jobs;
    }

    @Override
    public Jobs CreateJob_Log(int job_id,int status_id) {
        //tạo mới job_log, lúc sửa status chứ ko phải lúc phân việc , sau khi đã sauwr status thành đã nghiệm thu của mộc, sơn , nhám
        Jobs jobs_log = new Jobs();
        Jobs jobs_history = jobRepository.getJobById(job_id);
        jobs_log.setUser(jobs_history.getUser());
        jobs_log.setProduct(jobs_history.getProduct());
        jobs_log.setDescription(jobs_history.getDescription());
        jobs_log.setCost(jobs_history.getCost());
        jobs_log.setTimeStart(jobs_history.getTimeStart());
        jobs_log.setStatus(jobs_history.getStatus());
        jobs_log.setTimeFinish(jobs_history.getTimeFinish());
        jobs_log.setProduct(jobs_history.getProduct());
        jobs_log.setRequestProducts(jobs_history.getRequestProducts());
        jobs_log.setOrderdetails(jobs_history.getOrderdetails());
        jobs_log.setJob_name(jobs_history.getJob_name());
        jobs_log.setCode(jobs_history.getCode());
        jobs_log.setJob_log(true);
        jobRepository.save(jobs_log);
        return jobs_log;
    }

}

//note : co ne de status cua thang product luc add no la chua thuc hien
