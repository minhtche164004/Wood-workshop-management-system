package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "processproducterror", schema = "test1", catalog = "")
public class Processproducterror {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "process_product_error_id")
    private int processProductErrorId;
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "job_id")
    private Integer jobId;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "solution")
    private String solution;
    @Basic
    @Column(name = "is_fixed")
    private Byte isFixed;
    @Basic
    @Column(name = "code")
    private String code;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processproducterror that = (Processproducterror) o;
        return processProductErrorId == that.processProductErrorId && Objects.equals(productId, that.productId) && Objects.equals(jobId, that.jobId) && Objects.equals(description, that.description) && Objects.equals(solution, that.solution) && Objects.equals(isFixed, that.isFixed) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processProductErrorId, productId, jobId, description, solution, isFixed, code);
    }
}
