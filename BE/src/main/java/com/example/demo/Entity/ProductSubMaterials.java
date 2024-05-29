package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "product_sub_materials", schema = "test1", catalog = "")
public class ProductSubMaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_sub_material_id")
    private int productSubMaterialId;
    @Basic
    @Column(name = "sub_material_id")
    private Integer subMaterialId;
    @Basic
    @Column(name = "product_id")
    private Integer productId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSubMaterials that = (ProductSubMaterials) o;
        return productSubMaterialId == that.productSubMaterialId && Objects.equals(subMaterialId, that.subMaterialId) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productSubMaterialId, subMaterialId, productId);
    }
}
