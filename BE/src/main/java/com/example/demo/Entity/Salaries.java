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



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Salaries that = (Salaries) o;
        return salaryId == that.salaryId && Objects.equals(userId, that.userId) && Objects.equals(amount, that.amount) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salaryId, userId, amount, date);
    }
}
