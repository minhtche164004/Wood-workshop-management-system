package com.example.demo.Service.Impl;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.JobService;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private EntityManager entityManager;


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
        if (jobProductDTOS.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobProductDTOS;
    }

    @Override
    public Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id, int status_id, int job_id) {
        Jobs jobs = new Jobs();
        User user = userRepository.findByIdJob(user_id);
        jobs.setUser(user);
        Products products = productRepository.findById(p_id);
        RequestProducts requestProducts = requestProductRepository.findById(p_id);

        if (products == null) { //tức là đang phân job cho requets product
            jobs.setRequestProducts(requestProducts);
            jobs.setProduct(null);
        } else {////tức là đang phân job cho  product có sẵn
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
        Status_Job statusJob = statusJobRepository.findById(status_id); //set  status job theo input
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

        return jobs;
    }

    @Override
    public Jobs CreateJob_Log(int job_id, int status_id) {
        //tạo mới job_log, lúc sửa status chứ ko phải lúc phân việc , sau khi đã sauwr status thành đã nghiệm thu của mộc, sơn , nhám
        //sau khi nghiem thu xong thi tra ve trang thai chờ cong viec tiep theo
        Jobs jobs_log = new Jobs();
        Jobs jobs_history = jobRepository.getJobById(job_id);
        jobs_log.setUser(jobs_history.getUser());
        jobs_log.setProduct(jobs_history.getProduct());
        jobs_log.setDescription(jobs_history.getDescription());
        jobs_log.setCost(jobs_history.getCost());
        jobs_log.setTimeStart(jobs_history.getTimeStart());
        jobs_log.setStatus(statusJobRepository.findById(status_id)); //set status thanh da nghiem thu
        jobs_log.setTimeFinish(jobs_history.getTimeFinish());
        jobs_log.setRequestProducts(jobs_history.getRequestProducts());
        jobs_log.setOrderdetails(jobs_history.getOrderdetails());
        jobs_log.setJob_name(jobs_history.getJob_name());
        jobs_log.setCode(jobs_history.getCode());
        jobs_log.setJob_log(true); // true de ẩn không ra man` hinh`
        jobRepository.save(jobs_log);

        //cai nay` la de tao ra job tiep theo cho san pham va o trang thai dang cho giao cong viec tiep
        Jobs waitNextJob = jobRepository.findById(job_id).orElseThrow(() -> new RuntimeException("Job not found"));
        waitNextJob.setProduct(jobs_history.getProduct());
        waitNextJob.setRequestProducts(jobs_history.getRequestProducts());
        if(status_id == 12){
            jobs_log.setStatus(statusJobRepository.findById(13)); //khi da nghiem thu o cong doan cuoi la son thi` se chuyen status sang trang thai da hoan` thanh
        }
        else{
            waitNextJob.setStatus(statusJobRepository.findById(15)); // khi da nghiem thu thi se chuyen sang trang thai chờ cong viec tiep theo
        }
        waitNextJob.setJob_log(false); // false de hien thi ra man` hinh`
        jobRepository.save(waitNextJob);

        return jobs_log;
    }

    @Override
    public Jobs EditJobs(JobDTO jobDTO, int job_id) {
        Jobs jobs = jobRepository.getJobById(job_id);
        jobs.setJob_name(jobDTO.getJob_name());
        jobs.setCost(jobDTO.getCost());
        jobs.setDescription(jobDTO.getDescription());
        jobs.setQuantityProduct(jobDTO.getQuantity_product());
        jobs.setTimeFinish(jobDTO.getFinish());
        jobs.setTimeStart(jobDTO.getStart());
        jobRepository.save(jobs);
        entityManager.refresh(jobs);
        return jobs;
    }

    @Override
    public List<Jobs> GetAllJob() {
        List<Jobs> jobsList = jobRepository.findAll();
        if(jobsList == null ){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return jobsList;
    }

}

//note : co ne de status cua thang product luc add no la chua thuc hien
