package com.example.demo.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "request_products", schema = "test1", catalog = "")
public class RequestProducts {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_product_id")
    private int requestProductId;
    @Basic
    @Column(name = "request_product_name")
    private String requestProductName;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "price")
    private String price;
    @Basic
    @Column(name = "quantity")
    private Integer quantity;
    @Basic
    @Column(name = "completion_time")
    private Date completionTime;
    @Basic
    @Column(name = "image")
    private String image;



}
