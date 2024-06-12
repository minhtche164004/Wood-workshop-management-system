package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "employeematerials", schema = "test1", catalog = "")
public class EmployeematerialsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "emp_material_id")
    private int empMaterialId;
    @Basic
    @Column(name = "employee_id")
    private Integer employeeId;
    @Basic
    @Column(name = "material_id")
    private Integer materialId;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;

    public int getEmpMaterialId() {
        return empMaterialId;
    }

    public void setEmpMaterialId(int empMaterialId) {
        this.empMaterialId = empMaterialId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeematerialsEntity that = (EmployeematerialsEntity) o;
        return empMaterialId == that.empMaterialId && Objects.equals(employeeId, that.employeeId) && Objects.equals(materialId, that.materialId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empMaterialId, employeeId, materialId, quantity);
    }
}
