package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestOrder {
    private Integer special_order;
    private CusInfo cusInfo;
    private OrderDetailRequest orderDetailRequest;
    private OderDetail oderDetail;
    private Integer payment_method;

}
