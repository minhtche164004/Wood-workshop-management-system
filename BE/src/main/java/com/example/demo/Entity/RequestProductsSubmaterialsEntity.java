package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "request_products_submaterials", schema = "test1", catalog = "")
public class RequestProductsSubmaterialsEntity {
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

    public int getRequestProductsSubmaterialsId() {
        return requestProductsSubmaterialsId;
    }

    public void setRequestProductsSubmaterialsId(int requestProductsSubmaterialsId) {
        this.requestProductsSubmaterialsId = requestProductsSubmaterialsId;
    }

    public Integer getRequestProductId() {
        return requestProductId;
    }

    public void setRequestProductId(Integer requestProductId) {
        this.requestProductId = requestProductId;
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
        RequestProductsSubmaterialsEntity that = (RequestProductsSubmaterialsEntity) o;
        return requestProductsSubmaterialsId == that.requestProductsSubmaterialsId && Objects.equals(requestProductId, that.requestProductId) && Objects.equals(subMaterialId, that.subMaterialId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestProductsSubmaterialsId, requestProductId, subMaterialId);
    }
}
