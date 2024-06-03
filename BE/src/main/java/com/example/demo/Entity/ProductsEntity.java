package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "products", schema = "test1", catalog = "")
public class ProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private int productId;
    @Basic
    @Column(name = "product_name")
    private String productName;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "price")
    private BigDecimal price;
    @Basic
    @Column(name = "status_id")
    private Integer statusId;
    @Basic
    @Column(name = "completion_time")
    private String completionTime;
    @Basic
    @Column(name = "image")
    private String image;
    @Basic
    @Column(name = "category_id")
    private Integer categoryId;
    @Basic
    @Column(name = "enddate_warranty")
    private Date enddateWarranty;
    @Basic
    @Column(name = "code")
    private String code;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Date getEnddateWarranty() {
        return enddateWarranty;
    }

    public void setEnddateWarranty(Date enddateWarranty) {
        this.enddateWarranty = enddateWarranty;
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
        ProductsEntity that = (ProductsEntity) o;
        return productId == that.productId && Objects.equals(productName, that.productName) && Objects.equals(description, that.description) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(statusId, that.statusId) && Objects.equals(completionTime, that.completionTime) && Objects.equals(image, that.image) && Objects.equals(categoryId, that.categoryId) && Objects.equals(enddateWarranty, that.enddateWarranty) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productName, description, quantity, price, statusId, completionTime, image, categoryId, enddateWarranty, code);
    }
}
