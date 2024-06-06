package com.example.demo.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "salaries", schema = "test1", catalog = "")
public class Salaries {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "salary_id")
    private int salaryId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "amount")
    private BigDecimal amount;
    @Basic
    @Column(name = "date")
    private Date date;



}
