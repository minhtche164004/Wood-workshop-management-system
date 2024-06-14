package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request_products", schema = "test1", catalog = "")
public class RequestProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_product_id", nullable = false)
    private int requestProductId;

    @Column(name = "request_product_name", nullable = false, length = 50)
    private String requestProductName;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "price", precision = 19, scale = 4) // Increased precision and scale for wider range and decimals
    private BigDecimal price; // Changed to BigDecimal for accuracy

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "completion_time")
    private Date completionTime;

//    @Column(name = "image")
//    private String image;

    @ManyToOne
    @JoinColumn(name = "request_id", referencedColumnName = "request_id")
    private Requests requests;



}
