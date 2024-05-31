package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "suppliermaterial", schema = "test1", catalog = "")
public class Suppliermaterial {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "supplier_material")
    private int supplierMaterial;
    @Column(name = "supplier_name")
    private String supplierName;
    @Column(name = "phone_number")
    private String phoneNumber;
//    @Column(name = "sub_material_id")
//    private Integer subMaterialId;


    @ManyToOne // Một nhà cung cấp có thể cung cấp nhiều nguyên liệu phụ
    @JsonIgnore
    @JoinColumn(name = "sub_material_id") // Khóa ngoại liên kết với bảng sub_materials
    private SubMaterials subMaterial;



}
