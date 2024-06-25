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

    private String code;
    private int product_id;
    private String product_name;
    private String description;
    private BigDecimal price;
//    private String status_name;
//    private int status_id;
    private Status_Job statusJob;
    private int quantity;
    private User user;


    public JobProductDTO(String code, int product_id, String product_name, String description, BigDecimal price, Status_Job statusJob, int quantity, User user) {
        this.code = code;
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.statusJob = statusJob;
        this.quantity = quantity;
        this.user = user;
    }

}
