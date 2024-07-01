package com.example.demo.Dto.JobDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JobDoneDTO {
    private Integer job_id;
    private String job_name;
    private Integer user_id;
    private String user_name;
    private Integer position_id;
    private String position_name;
    private Integer status_job_id;
    private String status_job_name;
    private BigDecimal cost;
    private Integer product_id;
    private String product_name;
    private Integer request_id;
    private String request_name;
    private Integer quantity;


}
