package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "sub_materials", schema = "test1", catalog = "")
public class SubMaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sub_material_id")
    private int subMaterialId;

    @Column(name = "sub_material_name")
    private String subMaterialName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "code")
    private String code;


    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "material_id")
    private Materials materials;

}
