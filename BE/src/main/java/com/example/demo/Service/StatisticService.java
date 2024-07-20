package com.example.demo.Service;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface StatisticService {
    BigDecimal findTotalSalaryByMonthAndYear(int month, int year);
    Long countCompletedJobsByMonthAndYear(int status_id,int month, int year);
    Long countProduct();
    Long countRequestProduct();
    Long countSpecialOrder();
    Long countTotalOrderByMonthAndYear(int month, int year);
    Long countTotalSpecialOrderByMonthAndYear(int month, int year);
    Long countCompletedOrderByMonthAndYear(int month, int year);
    Long countOrderHaveDone(int query);
    Long countEmployeeWithTypePosition(int query);
    BigDecimal totalAmountOrderHaveDone();
    BigDecimal totalAmountSubMaterial();
    BigDecimal findTotalSubMaterialByMonthAndYear(int year, int month);
    Integer countCompletedJobsForProductByMonthAndYear(int status_id,int month, int year);
    Integer countCompletedJobsForRequestProductByMonthAndYear(int status_id,int month, int year);
    Long countTotalOrder();
    Long countCompletedProductOnOrderByMonthAndYear(int year, int month);
    Long countCompletedRequestProductOnOrderByMonthAndYear(int year, int month);



}
