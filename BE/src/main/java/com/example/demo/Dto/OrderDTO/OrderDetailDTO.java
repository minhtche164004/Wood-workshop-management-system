package com.example.demo.Dto.OrderDTO;

import com.example.demo.Entity.Status_Product;
import com.example.demo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private String code;
    private int product_id;
    private String product_name;
    private String description;
    private BigDecimal price;
//    private String status_name;
//    private int status_id;
    private Status_Product statusProduct;
    private int quantity;
    private User user;

    public OrderDetailDTO(String code, int product_id, String product_name, String description, BigDecimal price,Status_Product statusProduct, int quantity) {
        this.code = code;
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.statusProduct=statusProduct;
        this.quantity = quantity;
    }

    public OrderDetailDTO(String code, int product_id, String product_name, String description, BigDecimal price, Status_Product statusProduct, int quantity, User user) {
        this.code = code;
        this.product_id = product_id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.statusProduct = statusProduct;
        this.quantity = quantity;
        this.user = user;
    }
}
