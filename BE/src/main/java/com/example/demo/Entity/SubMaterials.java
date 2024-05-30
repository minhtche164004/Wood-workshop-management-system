package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sub_materials", schema = "test1", catalog = "")
public class SubMaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sub_material_id")
    private int subMaterialId;
    @Basic
    @Column(name = "sub_material_name")
    private String subMaterialName;
    @Basic
    @Column(name = "material_id")
    private Integer materialId;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Basic
    @Column(name = "code")
    private String code;


}
