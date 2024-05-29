package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "orderdetails", schema = "test1", catalog = "")
public class Orderdetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_detail_id")
    private int orderDetailId;
    @Basic
    @Column(name = "order_id")
    private Integer orderId;
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "unit_price")
    private BigDecimal unitPrice;
    @Basic
    @Column(name = "request_product_id")
    private Integer requestProductId;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orderdetails that = (Orderdetails) o;
        return orderDetailId == that.orderDetailId && Objects.equals(orderId, that.orderId) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity) && Objects.equals(unitPrice, that.unitPrice) && Objects.equals(requestProductId, that.requestProductId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDetailId, orderId, productId, quantity, unitPrice, requestProductId);
    }
}
