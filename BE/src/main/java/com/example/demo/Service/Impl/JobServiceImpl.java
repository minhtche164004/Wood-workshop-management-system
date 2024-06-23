package com.example.demo.Service.Impl;

import com.example.demo.Dto.JobDTO.JobDTO;
import com.example.demo.Entity.Jobs;
import com.example.demo.Repository.JobRepository;
import com.example.demo.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    private JobRepository jobRepository;

    @Override
    public Jobs CreateJobForEmployee(JobDTO jobDTO) {
        return null;
    }


    //manager view 1 list orderDetail(bao gồm các sản phẩm và số lượng của nó có trong đơn hàng , 1 order thì có thể có nhiều orderDetail)
    //trong list ấy , manager sẽ bấm vào thao tác phân việc



}
