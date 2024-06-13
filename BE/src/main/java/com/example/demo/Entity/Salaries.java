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
@Table(name = "salaries", schema = "test1", catalog = "")
public class Salaries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id", nullable = false)
    private int salaryId;

    @ManyToOne // Relationship with Users entity
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Assuming you have a Users entity

    @Column(name = "amount", precision = 38, scale = 2) // Assuming monetary value with precision and scale
    private BigDecimal amount;

    @Column(name = "date")
    private Date date;



}
