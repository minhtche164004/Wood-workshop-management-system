package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data

@Entity
@Table(name = "employeematerials", schema = "test1", catalog = "")
public class Employeematerials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "emp_material_id")
    private int empMaterialId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name="product_sub_material_id")
    private Integer product_sub_material_id;


}
