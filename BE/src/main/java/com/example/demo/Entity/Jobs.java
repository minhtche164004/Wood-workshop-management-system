package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "jobs", schema = "test1", catalog = "")
public class Jobs {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "job_id")
    private int jobId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "product_id")
    private Integer productId;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "time_finish")
    private Timestamp timeFinish;
    @Basic
    @Column(name = "quantity_product")
    private Integer quantityProduct;
    @Basic
    @Column(name = "cost")
    private BigDecimal cost;
    @Basic
    @Column(name = "time_start")
    private Date timeStart;
    @Basic
    @Column(name = "code")
    private String code;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Jobs that = (Jobs) o;
        return jobId == that.jobId && Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId) && Objects.equals(description, that.description) && Objects.equals(timeFinish, that.timeFinish) && Objects.equals(quantityProduct, that.quantityProduct) && Objects.equals(cost, that.cost) && Objects.equals(timeStart, that.timeStart) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, userId, productId, description, timeFinish, quantityProduct, cost, timeStart, code);
    }
}
