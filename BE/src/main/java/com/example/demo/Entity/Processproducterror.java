package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "process_product_error", schema = "test1", catalog = "")
public class Processproducterror {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "process_product_error_id")
    private int processProductErrorId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Products product; // Liên kết với Products

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "job_id")
    private Jobs job; // Liên kết với Jobs

    @Column(name = "description")
    private String description;

    @Column(name = "solution")
    private String solution;

    @Column(name = "is_fixed")
    private Boolean isFixed; // Sử dụng Boolean cho trường kiểu tinyint

    @Column(name = "code")
    private String code;


}
/*
1 product có thể có nhiều lỗi
 */
