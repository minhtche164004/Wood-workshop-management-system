package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestSpecialOrder {
//    private Integer special_order;
//    private ReceiveInfo receiveInfo;
    private Integer payment_method;
    private Date orderFinish;
}
