package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "suppliermaterial", schema = "test1", catalog = "")
public class SuppliermaterialEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "supplier_material")
    private int supplierMaterial;
    @Basic
    @Column(name = "supplier_name")
    private String supplierName;
    @Basic
    @Column(name = "phone_number")
    private String phoneNumber;
    @Basic
    @Column(name = "sub_material_id")
    private Integer subMaterialId;

    public int getSupplierMaterial() {
        return supplierMaterial;
    }

    public void setSupplierMaterial(int supplierMaterial) {
        this.supplierMaterial = supplierMaterial;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getSubMaterialId() {
        return subMaterialId;
    }

    public void setSubMaterialId(Integer subMaterialId) {
        this.subMaterialId = subMaterialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuppliermaterialEntity that = (SuppliermaterialEntity) o;
        return supplierMaterial == that.supplierMaterial && Objects.equals(supplierName, that.supplierName) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(subMaterialId, that.subMaterialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierMaterial, supplierName, phoneNumber, subMaterialId);
    }
}
