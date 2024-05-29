package com.example.demo.Entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "requests", schema = "test1", catalog = "")
public class Requests {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_id")
    private int requestId;
    @Basic
    @Column(name = "user_id")
    private Integer userId;
    @Basic
    @Column(name = "request_date")
    private Timestamp requestDate;
    @Basic
    @Column(name = "status_id")
    private Integer statusId;
    @Basic
    @Column(name = "response")
    private String response;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "code")
    private String code;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requests that = (Requests) o;
        return requestId == that.requestId && Objects.equals(userId, that.userId) && Objects.equals(requestDate, that.requestDate) && Objects.equals(statusId, that.statusId) && Objects.equals(response, that.response) && Objects.equals(description, that.description) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, userId, requestDate, statusId, response, description, code);
    }
}
