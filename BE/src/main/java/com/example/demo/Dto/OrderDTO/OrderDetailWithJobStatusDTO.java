package com.example.demo.Dto.OrderDTO;

import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.Status_Job;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailWithJobStatusDTO {
    private Orderdetails orderdetail;
    private Status_Job statusJob;
    private String productNameOrRequestProductName;

    public OrderDetailWithJobStatusDTO(Orderdetails orderdetail, Status_Job statusJob, String productNameOrRequestProductName) {
        this.orderdetail = orderdetail;
        this.statusJob = statusJob;
        this.productNameOrRequestProductName = productNameOrRequestProductName;
    }
}