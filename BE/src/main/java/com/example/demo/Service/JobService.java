package com.example.demo.Service;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.OrderDetailDTO;
import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Orderdetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

//    Jobs CreateJobForEmployee(JobDTO jobDTO);
    List<OrderDetailDTO> getListRequestProductJob();

    List<OrderDetailDTO> getListProductJob();

    List<OrderDetailDTO> getListProductJobByNameOrCode(String key);

    List<OrderDetailDTO> getRequestProductInOrderDetailByCode(String code);
    Jobs CreateJob(JobDTO jobDTO,int user_id,int p_id,int status_id);
}
