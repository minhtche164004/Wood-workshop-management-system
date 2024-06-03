package com.example.demo.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrdersDTO {
    private Integer orderId;
    private LocalDateTime orderDate;
    private Integer statusId;
    private BigDecimal totalAmount;
    private Byte specialOrder;
    private Byte paymentMethod;
    private BigDecimal deposite;
    private Integer inforId;
    private String code;

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getStatusId() {
        return this.statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getSpecialOrder() {
        return this.specialOrder;
    }

    public void setSpecialOrder(Byte specialOrder) {
        this.specialOrder = specialOrder;
    }

    public Byte getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(Byte paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getDeposite() {
        return this.deposite;
    }

    public void setDeposite(BigDecimal deposite) {
        this.deposite = deposite;
    }

    public Integer getInforId() {
        return this.inforId;
    }

    public void setInforId(Integer inforId) {
        this.inforId = inforId;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
