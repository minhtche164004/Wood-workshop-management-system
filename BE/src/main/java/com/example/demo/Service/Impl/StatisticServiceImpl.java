package com.example.demo.Service.Impl;

import com.example.demo.Entity.SubMaterials;
import com.example.demo.Repository.AdvancesalaryRepository;
import com.example.demo.Repository.InputSubMaterialRepository;
import com.example.demo.Repository.SubMaterialsRepository;
import com.example.demo.Service.StatisticService;
import com.nimbusds.jwt.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {
    @Autowired
    private AdvancesalaryRepository advancesalaryRepository;
    @Autowired
    private SubMaterialsRepository subMaterialsRepository;
    @Autowired
    private InputSubMaterialRepository inputSubMaterialRepository;

    @Override
    public BigDecimal findTotalSalaryByMonthAndYear(int month, int year) {
        BigDecimal result =advancesalaryRepository.findTotalSalaryByMonthAndYear(month,year);
        return result ;
    }

    @Override
    public BigDecimal TotalSalaryNotPayment(int year) {
        BigDecimal result = advancesalaryRepository.TotalSalaryNotPayment(year);
        return result;
    }

    @Override
    public BigDecimal TotalSalaryAllPayment(int year) {
        BigDecimal result = advancesalaryRepository.TotalSalaryAllPayment(year);
        return result;
    }



    @Override
    public Long countCompletedJobsByMonthAndYear(int status_id, int month, int year) {
        return advancesalaryRepository.countCompletedJobsByMonthAndYear(status_id,month,year);
    }
    @Override
    public Long countCompletedProductOnOrderByMonthAndYear( int month, int year) {
        return advancesalaryRepository.countCompletedProductOnOrderByMonthAndYear(month,year);
    }

    @Override
    public Long countCompletedRequestProductOnOrderByMonthAndYear( int month, int year) {
        return advancesalaryRepository.countCompletedRequestProductOnOrderByMonthAndYear(month,year);
    }




    @Override
    public Integer countCompletedJobsForProductByMonthAndYear(int status_id, int month, int year) {
        return advancesalaryRepository.countCompletedJobsForProductByMonthAndYear(status_id,month,year);
    }
    @Override
    public Integer countCompletedJobsForRequestProductByMonthAndYear(int status_id, int month, int year) {
        return advancesalaryRepository.countCompletedJobsForRequestProductByMonthAndYear(status_id,month,year);
    }

    @Override
    public Long countProduct() {
        return advancesalaryRepository.countProduct();
    }
    @Override
    public Long countTotalOrder() {
        return advancesalaryRepository.countTotalOrder();
    }
    @Override
    public Long countRequestProduct() {
        return advancesalaryRepository.countRequestProduct();
    }

    @Override
    public Long countSpecialOrder() {
        return advancesalaryRepository.countSpecialOrder();
    }

    @Override
    public Long countTotalOrderByMonthAndYear(int month, int year) {
        return advancesalaryRepository.countTotalOrderByMonthAndYear( month,  year);
    }
    @Override
    public Long countCompletedOrderByMonthAndYear(int month, int year) {
        return advancesalaryRepository.countCompletedOrderByMonthAndYear( month,  year);
    }
    @Override
    public Long countTotalSpecialOrderByMonthAndYear(int month, int year) {
        return advancesalaryRepository.countTotalSpecialOrderByMonthAndYear( month,  year);
    }

    @Override
    public Long countOrderHaveDone(int query) {
        return advancesalaryRepository.countOrderHaveDone(query);
    }

    @Override
    public Long countEmployeeWithTypePosition(int query) {
        return advancesalaryRepository.countEmployeeWithTypePosition(query);
    }

    @Override
    public BigDecimal totalAmountOrderHaveDone(int year) {
        BigDecimal totalAmountOrderHaveDone = advancesalaryRepository.totalAmountOrderHaveDone(year);
        BigDecimal totalAmountOrderRefund = advancesalaryRepository.totalAmountOrderRefund(year);
        BigDecimal totalAmountOrderCancel = advancesalaryRepository.totalAmountOrderCancel(year);

        BigDecimal a = null;
        if (totalAmountOrderHaveDone != null) {
            a = totalAmountOrderHaveDone;
        }

        if (totalAmountOrderRefund != null && a != null) {
            a = a.subtract(totalAmountOrderRefund);
        }

        if (totalAmountOrderCancel != null && a != null) {
            a = a.add(totalAmountOrderCancel);
        }

        return a;
    }

    @Override
    public BigDecimal totalAmountSubMaterial() {
        return advancesalaryRepository.totalAmountSubMaterial();
    }
    @Override
    public BigDecimal findTotalSubMaterialByMonthAndYear(int month, int year) {
        return advancesalaryRepository.findTotalSubMaterialByMonthAndYear(month, year);

    }
    @Override
    public BigDecimal findTotalInputSubMaterialByMonthAndYear(int month, int year) { // đây là số tiền nhập nguyên liệu + số tiền nhập kho lúc thay đổi số lượng + số tiền nhập kho lúc thay đổi số lượng và giá
        return advancesalaryRepository.findTotalSubMaterialByMonthAndYear(month, year);
    }

}
