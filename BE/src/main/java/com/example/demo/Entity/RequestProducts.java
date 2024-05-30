package com.example.demo.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "request_products", schema = "test1", catalog = "")
public class RequestProducts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_product_id")
    private int requestProductId;
    @Basic
    @Column(name = "request_product_name")
    private String requestProductName;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "price")
    private String price;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "completion_time")
    private Date completionTime;
    @Basic
    @Column(name = "image")
    private String image;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestProducts that = (RequestProducts) o;
        return requestProductId == that.requestProductId && Objects.equals(requestProductName, that.requestProductName) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(quantity, that.quantity) && Objects.equals(completionTime, that.completionTime) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestProductId, requestProductName, description, price, quantity, completionTime, image);
    }
}
