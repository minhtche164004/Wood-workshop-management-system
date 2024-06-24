package com.example.demo.Service.Impl;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.JobService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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


    @Override
    public List<OrderDetailDTO> getListRequestProductJob() {
List<OrderDetailDTO> orderdetailsList = orderDetailRepository.getRequestProductInOrderDetail();
        return orderdetailsList;
    }

    @Override
    public List<OrderDetailDTO> getListProductJob() {
        List<Products> productsList = productRepository.findAll();
        List<OrderDetailDTO> orderDetailsList = new ArrayList<>();

        for (Products product : productsList) {
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setProduct_id(product.getProductId());
            dto.setDescription(product.getDescription());
            dto.setProduct_name(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setQuantity(product.getQuantity());
            dto.setCode(null);

            dto.setStatusProduct(product.getStatus());
            orderDetailsList.add(dto); // Thêm dto vào danh sách
        }
        return orderDetailsList;
    }

    @Override
    public List<OrderDetailDTO> getListProductJobByNameOrCode(String key) {
        List<Products> productsList = productRepository.findProductByNameCode(key);
        List<OrderDetailDTO> orderDetailsList = new ArrayList<>();

        for (Products product : productsList) {
            OrderDetailDTO dto = new OrderDetailDTO();
            dto.setProduct_id(product.getProductId());
            dto.setDescription(product.getDescription());
            dto.setProduct_name(product.getProductName());
            dto.setPrice(product.getPrice());
            dto.setQuantity(product.getQuantity());
            dto.setCode(null);

            dto.setStatusProduct(product.getStatus());
            orderDetailsList.add(dto); // Thêm dto vào danh sách
        }
        return orderDetailsList;
    }

    @Override
    public List<OrderDetailDTO> getRequestProductInOrderDetailByCode(String code) {
        List<OrderDetailDTO> orderDetailDTOS = orderDetailRepository.getRequestProductInOrderDetailByCode(code);
        if(orderDetailDTOS.isEmpty()){
            throw  new AppException(ErrorCode.NOT_FOUND);
        }
        return orderDetailDTOS;
    }

    @Override
    public Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id,int status_id) {
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
        jobs.setTimeStart(jobDTO.getStart());
        Status_Product statusProduct = statusProductRepository.findById(5); //set mac dinh la job dang thi cong
        jobs.setStatus(statusProduct);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        Jobs lastJob = jobRepository.findJobsTop(dateString + "JB");
        int count = lastJob != null ? Integer.parseInt(lastJob.getCode().substring(8)) + 1 : 1;
        String code = dateString + "JB" + String.format("%03d", count);
        jobs.setCode(code);
        jobRepository.save(jobs);
        //nếu là giao việc thành công thì sẽ chuyển trạng thái của sản phẩm
        if(status_id == 11 && products ==null){//chưa giao việc  -> phân đang làm mộc
            requestProducts.setStatus(statusProductRepository.findById(7));
            requestProductRepository.save(requestProducts);
        }
        if(status_id == 11 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(7));
            productRepository.save(products);
        }

        if(status_id == 7 && products ==null){// đang làm mộc ->  phân đang đánh nhám(tức là công đoạn đang làm mộc đã xong)
            requestProducts.setStatus(statusProductRepository.findById(8));
            requestProductRepository.save(requestProducts);

        }
        if(status_id == 7 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(8));
            productRepository.save(products);
        }

        if(status_id == 8 && products ==null){//đang đánh nhám ->phân đang sơn(tức là công đoạn đang đánh nhám đã xong)
            requestProducts.setStatus(statusProductRepository.findById(9));
            requestProductRepository.save(requestProducts);
        }
        if(status_id == 8 && requestProducts == null){
            products.setStatus(statusProductRepository.findById(9));
            productRepository.save(products);
        }

        return jobs;

    }

}

//note : co ne de status cua thang product luc add no la chua thuc hien
