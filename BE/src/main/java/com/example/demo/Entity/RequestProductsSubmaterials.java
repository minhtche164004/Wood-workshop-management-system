package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "request_products_submaterials", schema = "test1", catalog = "")
public class RequestProductsSubmaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_products_submaterials_id")
    private int requestProductsSubmaterialsId;
    @Basic
    @Column(name = "request_product_id")
    private Integer requestProductId;
    @Basic
    @Column(name = "sub_material_id")
    private Integer subMaterialId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestProductsSubmaterials that = (RequestProductsSubmaterials) o;
        return requestProductsSubmaterialsId == that.requestProductsSubmaterialsId && Objects.equals(requestProductId, that.requestProductId) && Objects.equals(subMaterialId, that.subMaterialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestProductsSubmaterialsId, requestProductId, subMaterialId);
    }
}
