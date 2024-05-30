package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "status")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer status_id;
    @Column(name = "status_name", nullable = false)
    private String status_name;
    @JsonIgnore
    @OneToMany(mappedBy = "status",  cascade = CascadeType.ALL)
    @JsonBackReference
    private List<User> user;

    @OneToMany(mappedBy = "status",cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Products> products;
}
//