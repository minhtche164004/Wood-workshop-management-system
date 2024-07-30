package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data

@Entity
@Table(name = "employee_materials", schema = "test1", catalog = "")
public class Employeematerials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_material_id")
    private int empMaterialId;

    @Column(name = "total_material")
    private double total_material;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "product_sub_material_id") // Liên kết với ProductSubMaterials
    private ProductSubMaterials productSubMaterial;

    @ManyToOne
    @JoinColumn(name = "request_products_submaterials_id") // Liên kết RequestProductsSubmaterials
    private RequestProductsSubmaterials requestProductsSubmaterials;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "job_id")
    private Jobs jobs;

}

/*

Một nhân viên (User) có thể có nhiều bản ghi vật liệu (EmployeeMaterials).
Một vật liệu (Materials) có thể được sử dụng bởi nhiều nhân viên (EmployeeMaterials).
Bảng EmployeeMaterials đóng vai trò là bảng trung gian để lưu trữ thông tin về mối quan hệ giữa
nhân viên và vật liệu, bao gồm cả số lượng vật liệu mà nhân viên đó sử dụng.*/
