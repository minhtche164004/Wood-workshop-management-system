package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "action_type", schema = "test1", catalog = "")
public class Action_Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "action_type_id")
    private Integer action_type_id;

    @Column(name = "action_name")
    private String action_name;

}
