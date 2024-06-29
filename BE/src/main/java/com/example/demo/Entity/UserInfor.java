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

    @Column(name = "bank")
    private String bank_name;

    @Column(name = "bank_account_number")
    private String bank_number;

    @Column(name = "city_province")
    private String city_province;

    @Column(name = "district")
    private String district;

    @Column(name = "wards")
    private String wards;

    @Column(name = "has_Account")
    private Integer has_Account;  //1 là đã có account , 0 là chưa có


//    public UserInfor(String fullname, String phoneNumber, String address) {
//        this.fullname = fullname;
//        this.phoneNumber = phoneNumber;
//        this.address = address;
//    }

    public UserInfor(String phoneNumber, String fullname, String address, String bank_name, String bank_number, String city_province, String district, String wards,Integer has_Account) {
        this.phoneNumber = phoneNumber;
        this.fullname = fullname;
        this.address = address;
        this.bank_name = bank_name;
        this.bank_number = bank_number;
        this.city_province = city_province;
        this.district = district;
        this.wards = wards;
        this.has_Account=has_Account;
    }
}
