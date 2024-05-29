package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "sub_materials", schema = "test1", catalog = "")
public class SubMaterialsEntity {
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

    public int getSubMaterialId() {
        return subMaterialId;
    }

    public void setSubMaterialId(int subMaterialId) {
        this.subMaterialId = subMaterialId;
    }

    public String getSubMaterialName() {
        return subMaterialName;
    }

    public void setSubMaterialName(String subMaterialName) {
        this.subMaterialName = subMaterialName;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubMaterialsEntity that = (SubMaterialsEntity) o;
        return subMaterialId == that.subMaterialId && Objects.equals(subMaterialName, that.subMaterialName) && Objects.equals(materialId, that.materialId) && Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subMaterialId, subMaterialName, materialId, description, quantity, unitPrice, code);
    }
}
