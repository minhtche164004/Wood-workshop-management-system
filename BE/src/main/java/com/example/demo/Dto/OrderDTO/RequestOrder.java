package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestOrder {
    private Integer special_order;
    private CusInfo cusInfo;
    private OrderDetail orderDetail;
    private Integer payment_method;

}
