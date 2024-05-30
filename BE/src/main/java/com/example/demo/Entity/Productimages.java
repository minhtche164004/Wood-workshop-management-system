package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "productimages", schema = "test1", catalog = "")
public class Productimages {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_image_id")
    private int productImageId;
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "image")
    private String image;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Productimages that = (Productimages) o;
        return productImageId == that.productImageId && Objects.equals(productId, that.productId) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productImageId, productId, image);
    }
}
