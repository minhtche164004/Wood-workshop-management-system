package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Objects;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "requests", schema = "test1", catalog = "")
public class Requests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Assuming you have a Users entity

    @Column(name = "request_date")
    private Timestamp requestDate;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private Status_Request status;  // Assuming you have a Status entity

    @Column(name = "response")
    private String response;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;




}
