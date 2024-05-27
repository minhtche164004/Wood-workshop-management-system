package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "users")
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
    @Column(name = "status_id")
    private Integer status;
//    @Column(name = "position_id", length = 255)
//    private Integer position;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    @JsonBackReference  // Tránh vòng lặp vô hạn
    @ToString.Exclude  // Loại trừ thuộc tính users khỏi toString
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)// tức là khi tạo User thì infor cũng đc tạo
    @JoinColumn(name = "infor_id")
    @JsonBackReference
    private UserInfor userInfor;

    @OneToOne(cascade = CascadeType.ALL)// tức là khi tạo User thì infor cũng đc tạo
    @JoinColumn(name = "position_id")
    @JsonBackReference
    private Position position;
//
//    public User(){
//
//    }

        public User(String username, String password, String email, Integer status, Position position, Date hireDate, Role role,UserInfor userInfor) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = status;
        this.position = position;
        this.hireDate = hireDate;
        this.role = role;
        this.userInfor=userInfor;
    }


//    @Column(name = "phone_number", length = 20)
//    private String phoneNumber;


//    @Column(name = "address", length = 255)
//    private String address;

//    @Column(name = "fullname", length = 255)
//    private String fullname;

}
