package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username", nullable = false, length = 255)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;


    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "fullname", length = 255)
    private String fullname;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "position", length = 255)
    private String position;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference  // Tránh vòng lặp vô hạn
    @ToString.Exclude  // Loại trừ thuộc tính users khỏi toString
    private Role role;

        public User(String username, String password, String email, String phoneNumber, String address, String fullname, Boolean status, String position, Date hireDate, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.fullname = fullname;
        this.status = status;
        this.position = position;
        this.hireDate = hireDate;
        this.role = role;
    }

}
