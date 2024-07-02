package com.example.demo.Dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductErrorAllDTO {
    private int id;
    private String code;
    private String des;
    private boolean is_fix;
    private String solution;
    private String job_name;
    private int job_id;
    private int product_id;
    private String product_name;
    private int request_product_id;
    private String request_product_name;
    private String code_order;
    private String user_name_order;
    private String employee_name;
    
    public ProductErrorAllDTO(int id, String code, String des, boolean is_fix, String solution, String job_name, int job_id, int product_id, String product_name, int request_product_id, String request_product_name) {
        this.id = id;
        this.code = code;
        this.des = des;
        this.is_fix = is_fix;
        this.solution = solution;
        this.job_name = job_name;
        this.job_id = job_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.request_product_id = request_product_id;
        this.request_product_name = request_product_name;
    }

    public ProductErrorAllDTO(String code, String des, String solution, String job_name, String product_name, String request_product_name,
                              String code_order, String user_name_order, String employee_name) {
        this.code = code;
        this.des = des;
        this.solution = solution;
        this.job_name = job_name;
        this.product_name = product_name;
        this.request_product_name = request_product_name;
        this.code_order = code_order;
        this.user_name_order = user_name_order;
        this.employee_name = employee_name;
    }
}
