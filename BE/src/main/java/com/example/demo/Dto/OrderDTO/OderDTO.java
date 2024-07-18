package com.example.demo.Dto.OrderDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OderDTO  {
    private String code;
    private int orderId;
    private Date orderDate;
    private BigDecimal totalAmount;
    private int status_id;
    private String status_name;
    private int paymentMethod;
    private BigDecimal deposite;
    private boolean specialOrder;





}
