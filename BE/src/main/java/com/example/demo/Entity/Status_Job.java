package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "status_job")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Status_Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer status_id;
    @Column(name = "status_name", nullable = false)
    private String status_name;
    @Column(name = "type", nullable = false)
    private Integer type;
    @Column(name = "des")
    private String des;



}
