package com.example.demo.Service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface StatisticService {
    BigDecimal findTotalSalaryByMonthAndYear(int month, int year);
    Long countCompletedJobsByMonthAndYear(String status_name,int month, int year);
    Long countProduct();
    Long countSpecialOrder();
    Long countTotalOrder();
    Long countOrderHaveDone(int query);
    Long countEmployeeWithTypePosition(int query);
    BigDecimal totalAmountOrderHaveDone();
    BigDecimal totalAmountSubMaterial();

}
