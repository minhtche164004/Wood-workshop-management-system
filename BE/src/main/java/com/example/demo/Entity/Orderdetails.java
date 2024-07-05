package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orderdetails", schema = "test1", catalog = "")
public class Orderdetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "order_detail_id", nullable = false)
    private int orderDetailId;

    @ManyToOne // Relationship with Orders entity
    @JsonIgnore
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Orders order;  // Assuming you have an Orders entity

    @ManyToOne // Relationship with Products entity
    @JsonIgnore
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Products product;  // Assuming you have a Products entity

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price", precision = 38, scale = 2)
    private BigDecimal unitPrice;

    @ManyToOne // Relationship with RequestProducts entity
    @JsonIgnore
    @JoinColumn(name = "request_product_id", referencedColumnName = "request_product_id")
    private RequestProducts requestProduct; // Assuming you have a RequestProducts entity


    @OneToMany(mappedBy = "orderdetails")
    @JsonIgnore // Tùy chọn: bỏ qua trường này khi serialize thành JSON
    private List<Jobs> jobs;  // Một chi tiết đơn hàng có thể có nhiều công việc


}
