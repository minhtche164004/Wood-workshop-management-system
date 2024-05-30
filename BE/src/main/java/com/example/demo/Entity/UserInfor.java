package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "information_user")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "infor_id")
    private Integer inforId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "address")
    private String address;
    @JsonIgnore
    @OneToOne(mappedBy = "userInfor")
    @JsonBackReference
    private User user;


    public UserInfor(String fullname, String phoneNumber, String address) {
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}
