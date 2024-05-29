package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "materials", schema = "test1", catalog = "")
public class MaterialsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "material_id")
    private int materialId;
    @Basic
    @Column(name = "material_name")
    private String materialName;

    public int getMaterialId() {
        return materialId;
    }

    public void setMaterialId(int materialId) {
        this.materialId = materialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialsEntity that = (MaterialsEntity) o;
        return materialId == that.materialId && Objects.equals(materialName, that.materialName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, materialName);
    }
}
