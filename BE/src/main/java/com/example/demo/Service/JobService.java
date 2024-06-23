package com.example.demo.Service;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Entity.Jobs;
import org.springframework.stereotype.Service;

@Service
public interface JobService {

    Jobs CreateJobForEmployee(JobDTO jobDTO);
}
