package com.example.demo.Service;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Entity.Jobs;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobService {

//    Jobs CreateJobForEmployee(JobDTO jobDTO);
    List<JobProductDTO> getListRequestProductJob();
//
    List<JobProductDTO> getListProductJob();

    List<JobProductDTO> getListProductJobByNameOrCode(String key);
//
    List<JobProductDTO> getRequestProductInOrderDetailByCode(String code);
    Jobs CreateJob(JobDTO jobDTO, int user_id, int p_id, int status_id, int job_id);

    Jobs CreateJob_Log(int job_id,int status_id);

    Jobs EditJobs(JobDTO jobDTO,int job_id);
}
