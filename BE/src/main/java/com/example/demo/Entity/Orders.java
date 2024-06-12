package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders", schema = "test1", catalog = "")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @ManyToOne // Relationship with Status entity
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private Status_Order status;  // Assuming you have a Status entity

    @Column(name = "total_amount", precision = 38, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "special_order")
    private Boolean specialOrder; // Using Boolean for clarity

    @Column(name = "payment_method")
    private Integer paymentMethod; // Assuming you'll use an enum later

    @Column(name = "deposite")
    private BigDecimal deposite;

    @ManyToOne // Relationship with InformationUser entity
    @JoinColumn(name = "infor_id", referencedColumnName = "infor_id")
    private UserInfor userInfor; // Assuming you have an InformationUser entity

    @Column(name = "code")
    private String code;




}
