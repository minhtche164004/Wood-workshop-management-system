package com.example.demo.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "request_products", schema = "test1", catalog = "")
public class RequestProductsEntity {
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

    public int getRequestProductId() {
        return requestProductId;
    }

    public void setRequestProductId(int requestProductId) {
        this.requestProductId = requestProductId;
    }

    public String getRequestProductName() {
        return requestProductName;
    }

    public void setRequestProductName(String requestProductName) {
        this.requestProductName = requestProductName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Date getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestProductsEntity that = (RequestProductsEntity) o;
        return requestProductId == that.requestProductId && Objects.equals(requestProductName, that.requestProductName) && Objects.equals(description, that.description) && Objects.equals(price, that.price) && Objects.equals(quantity, that.quantity) && Objects.equals(completionTime, that.completionTime) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestProductId, requestProductName, description, price, quantity, completionTime, image);
    }
}
