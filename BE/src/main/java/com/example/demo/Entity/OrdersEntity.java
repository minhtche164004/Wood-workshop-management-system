package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "orders", schema = "test1", catalog = "")
public class OrdersEntity {
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getSpecialOrder() {
        return specialOrder;
    }

    public void setSpecialOrder(Byte specialOrder) {
        this.specialOrder = specialOrder;
    }

    public Byte getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Byte paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getDeposite() {
        return deposite;
    }

    public void setDeposite(BigDecimal deposite) {
        this.deposite = deposite;
    }

    public Integer getInforId() {
        return inforId;
    }

    public void setInforId(Integer inforId) {
        this.inforId = inforId;
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
        OrdersEntity that = (OrdersEntity) o;
        return orderId == that.orderId && Objects.equals(orderDate, that.orderDate) && Objects.equals(statusId, that.statusId) && Objects.equals(totalAmount, that.totalAmount) && Objects.equals(specialOrder, that.specialOrder) && Objects.equals(paymentMethod, that.paymentMethod) && Objects.equals(deposite, that.deposite) && Objects.equals(inforId, that.inforId) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderDate, statusId, totalAmount, specialOrder, paymentMethod, deposite, inforId, code);
    }
}
