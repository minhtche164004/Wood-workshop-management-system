package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "refund_order_status", schema = "test1", catalog = "")
public class RefundStatus {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "refund_id")
    private int refundId;
    @Basic
    @Column(name = "refund_name")
    private String refundName;
}
