package com.example.demo.Dto.OrderDTO;

import com.example.demo.Entity.Status_Job;
import com.example.demo.Entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class JobProductDTO {
    private int job_id;
    private String code;
    private int product_id;
    private String product_name;
    private String description;
    private BigDecimal price;
//    private String status_name;
//    private int status_id;
    private Status_Job statusJob;
    private int quantity;
    private int user_id;
    private String user_name;
    private int position_id;
    private String position_name;
    private int product_error_id;


    public JobProductDTO(int job_id,String code, int product_id, String product_name, String description, BigDecimal price, Status_Job statusJob, int quantity, int user_id, String user_name, int position_id, String position_name,int product_error_id) {
        this.job_id=job_id;
        this.code = code;
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.statusJob = statusJob;
        this.quantity = quantity;
        this.user_id = user_id;
        this.user_name = user_name;
        this.position_id = position_id;
        this.position_name = position_name;
        this.product_error_id = product_error_id;
    }
}
