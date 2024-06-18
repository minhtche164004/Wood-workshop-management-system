package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
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

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;  // Assuming you have a Users entity

    @Column(name = "request_date")
    private Date requestDate;

//    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private Status_Request status;  // Assuming you have a Status entity

    @Column(name = "response")
    private String response;

    @Column(name = "description")
    private String description;

    @Column(name = "code")
    private String code;


    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "address")
    private String address;

    @Column(name = "city_province")
    private String city_province;

    @Column(name = "district")
    private String district;

    @Column(name = "wards")
    private String wards;

    
}
