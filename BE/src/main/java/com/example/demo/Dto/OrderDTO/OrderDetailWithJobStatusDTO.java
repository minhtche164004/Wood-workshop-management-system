package com.example.demo.Dto.OrderDTO;

import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.Status_Job;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderDetailWithJobStatusDTO {
    private int order_detail_id;
    private int product_id;
    private String product_name;
    private int request_product_id;
    private String request_product_name;
    private BigDecimal price;
    private int status_job_id;
    private String status_job_name;
    private int quantity;

    public OrderDetailWithJobStatusDTO(int order_detail_id, int product_id, String product_name, int request_product_id, String request_product_name, BigDecimal price,int status_job_id,String status_job_name ,int quantity) {
        this.order_detail_id = order_detail_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.request_product_id = request_product_id;
        this.request_product_name = request_product_name;
        this.price = price;
        this.status_job_id=status_job_id;
        this.status_job_name=status_job_name;
        this.quantity = quantity;
    }
}