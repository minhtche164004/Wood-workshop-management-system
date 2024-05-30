package com.example.demo.Entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "advancesalary", schema = "test1", catalog = "")
public class Advancesalary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "advance_salary_id")
    private int advanceSalaryId;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "amount")
    private Long amount;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "is_advance_success")
    private Byte isAdvanceSuccess;
    @Basic
    @Column(name = "is_approve")
    private Byte isApprove;
    @Basic
    @Column(name = "code")
    private String code;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advancesalary that = (Advancesalary) o;
        return advanceSalaryId == that.advanceSalaryId && Objects.equals(date, that.date) && Objects.equals(amount, that.amount) && Objects.equals(userId, that.userId) && Objects.equals(isAdvanceSuccess, that.isAdvanceSuccess) && Objects.equals(isApprove, that.isApprove) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(advanceSalaryId, date, amount, userId, isAdvanceSuccess, isApprove, code);
    }
}
