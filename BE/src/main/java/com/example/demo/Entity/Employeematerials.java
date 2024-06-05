package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;
@Data

@Entity
@Table(name = "employeematerials", schema = "test1", catalog = "")
public class Employeematerials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_material_id")
    private int empMaterialId;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private User employee;

    @ManyToOne
    @JoinColumn(name = "product_sub_material_id") // Liên kết với ProductSubMaterials
    private ProductSubMaterials productSubMaterial;




}

/*

Một nhân viên (User) có thể có nhiều bản ghi vật liệu (EmployeeMaterials).
Một vật liệu (Materials) có thể được sử dụng bởi nhiều nhân viên (EmployeeMaterials).
Bảng EmployeeMaterials đóng vai trò là bảng trung gian để lưu trữ thông tin về mối quan hệ giữa
nhân viên và vật liệu, bao gồm cả số lượng vật liệu mà nhân viên đó sử dụng.*/
