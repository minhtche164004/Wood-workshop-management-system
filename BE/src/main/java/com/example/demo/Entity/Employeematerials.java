package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "employeematerials", schema = "test1", catalog = "")
public class Employeematerials {
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employeematerials that = (Employeematerials) o;
        return empMaterialId == that.empMaterialId && Objects.equals(employeeId, that.employeeId) && Objects.equals(materialId, that.materialId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(empMaterialId, employeeId, materialId, quantity);
    }
}
