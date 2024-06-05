package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "advancesalary", schema = "test1", catalog = "")
public class Advancesalary {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "advance_salary_id")
    private int advanceSalaryId;

    @Column(name = "date")
    private Date date;

    @Column(name = "amount")
    private Long amount;

//    @Column(name = "user_id")
//    private Integer userId;

    @Column(name = "is_advance_success")
    private Byte isAdvanceSuccess;

    @Column(name = "is_approve")
    private Byte isApprove;

    @Column(name = "code")
    private String code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
