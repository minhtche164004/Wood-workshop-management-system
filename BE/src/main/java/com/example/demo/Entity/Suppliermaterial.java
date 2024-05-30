package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "suppliermaterial", schema = "test1", catalog = "")
public class Suppliermaterial {
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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suppliermaterial that = (Suppliermaterial) o;
        return supplierMaterial == that.supplierMaterial && Objects.equals(supplierName, that.supplierName) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(subMaterialId, that.subMaterialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(supplierMaterial, supplierName, phoneNumber, subMaterialId);
    }
}
