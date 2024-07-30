package com.example.demo.Service.Impl;

import com.example.demo.Entity.Employeematerials;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SharedData {
    private List<Employeematerials> employeeMaterialsList;

    public List<Employeematerials> getEmployeeMaterialsList() {
        return employeeMaterialsList;
    }

    public void setEmployeeMaterialsList(List<Employeematerials> employeeMaterialsList) {
        this.employeeMaterialsList = employeeMaterialsList;
    }
}