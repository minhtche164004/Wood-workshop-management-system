package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "test1", catalog = "")
public class Orders {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id")
    private int orderId;
    @Basic
    @Column(name = "order_date")
    private Timestamp orderDate;
    @Basic
    @Column(name = "status_id")
    private Integer statusId;
    @Basic
    @Column(name = "total_amount")
    private BigDecimal totalAmount;
    @Basic
    @Column(name = "special_order")
    private Byte specialOrder;
    @Basic
    @Column(name = "payment_method")
    private Byte paymentMethod;
    @Basic
    @Column(name = "deposite")
    private BigDecimal deposite;
    @Basic
    @Column(name = "infor_id")
    private Integer inforId;
    @Basic
    @Column(name = "code")
    private String code;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders that = (Orders) o;
        return orderId == that.orderId && Objects.equals(orderDate, that.orderDate) && Objects.equals(statusId, that.statusId) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(specialOrder, that.specialOrder) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(deposite, that.deposite) && Objects.equals(inforId, that.inforId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderDate, statusId, totalAmount, specialOrder, paymentMethod, deposite, inforId, code);
    }
}
