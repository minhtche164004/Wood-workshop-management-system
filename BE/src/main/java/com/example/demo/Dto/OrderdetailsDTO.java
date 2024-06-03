package com.example.demo.Dto;

import java.math.BigDecimal;

public class OrderdetailsDTO {
    private Integer orderDetailId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Integer requestProductId;

    public Integer getOrderDetailId() {
        return this.orderDetailId;
    }

    public void setOrderDetailId(Integer orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Integer getOrderId() {
        return this.orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getRequestProductId() {
        return this.requestProductId;
    }

    public void setRequestProductId(Integer requestProductId) {
        this.requestProductId = requestProductId;
    }
}
