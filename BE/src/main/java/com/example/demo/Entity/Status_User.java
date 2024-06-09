package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "status_user")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Status_User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer status_id;
    @Column(name = "status_name", nullable = false)
    private String status_name;
//    @JsonIgnore
//    @OneToMany(mappedBy = "status",  cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<User> user;
//
//    @OneToMany(mappedBy = "status",cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<Products> products;
}
//