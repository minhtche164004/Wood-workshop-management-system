package com.example.demo.Service;

import com.example.demo.Dto.RequestOrder;
import com.example.demo.Entity.Orders;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    Orders AddOrder(RequestOrder requestOrder);

}
