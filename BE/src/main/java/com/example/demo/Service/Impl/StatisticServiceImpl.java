package com.example.demo.Service.Impl;

import com.example.demo.Entity.SubMaterials;
import com.example.demo.Repository.AdvancesalaryRepository;
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

    @Override
    public BigDecimal findTotalSalaryByMonthAndYear(int month, int year) {
        return advancesalaryRepository.findTotalSalaryByMonthAndYear(month,year);
    }

    @Override
    public Long countCompletedJobsByMonthAndYear(String status_name, int month, int year) {
        return advancesalaryRepository.countCompletedJobsByMonthAndYear(status_name,month,year);
    }

    @Override
    public Long countProduct() {
        return advancesalaryRepository.countProduct();
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
    public Long countTotalOrder() {
        return advancesalaryRepository.countTotalOrder();
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
    public BigDecimal totalAmountOrderHaveDone() {
        return advancesalaryRepository.totalAmountOrderHaveDone();
    }

    @Override
    public BigDecimal totalAmountSubMaterial() {
        return advancesalaryRepository.totalAmountSubMaterial();
    }
    @Override
    public BigDecimal findTotalSubMaterialByMonthAndYear(int year, int month) {
          return advancesalaryRepository.findTotalSubMaterialByMonthAndYear(year, month);

    }


}
