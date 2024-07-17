package com.example.demo.Dto.OrderDTO;

import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Status_Product;
import com.example.demo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data

@NoArgsConstructor
public class OrderDetailDTO {
    private int order_detail_id;
    private int product_id;
    private String product_name;
    private int request_product_id;
    private String request_product_name;
    private BigDecimal price;
    private int quantity;
    private Orders orders;

    public OrderDetailDTO(int order_detail_id, int product_id, String product_name, int request_product_id, String request_product_name, BigDecimal price,int quantity,Orders orders) {
        this.order_detail_id = order_detail_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.request_product_id = request_product_id;
        this.request_product_name = request_product_name;
        this.price = price;
        this.quantity = quantity;
        this.orders=orders;
    }
}



