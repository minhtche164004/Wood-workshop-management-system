package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "positions")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "position_id")
    private Integer position_id;

    @Column(name = "position_name")
    private String position_name;

@JsonIgnore
    @OneToMany(mappedBy = "position",  cascade = CascadeType.ALL)
@JsonBackReference
    private List<User> user;


}
