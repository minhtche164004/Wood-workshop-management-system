package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "processproducterror", schema = "test1", catalog = "")
public class ProcessproducterrorEntity {
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

    public int getProcessProductErrorId() {
        return processProductErrorId;
    }

    public void setProcessProductErrorId(int processProductErrorId) {
        this.processProductErrorId = processProductErrorId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Byte getIsFixed() {
        return isFixed;
    }

    public void setIsFixed(Byte isFixed) {
        this.isFixed = isFixed;
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
        ProcessproducterrorEntity that = (ProcessproducterrorEntity) o;
        return processProductErrorId == that.processProductErrorId && Objects.equals(productId, that.productId) && Objects.equals(jobId, that.jobId) && Objects.equals(description, that.description) && Objects.equals(solution, that.solution) && Objects.equals(isFixed, that.isFixed) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processProductErrorId, productId, jobId, description, solution, isFixed, code);
    }
}
