package com.example.demo.Entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "materials", schema = "test1", catalog = "")
public class Materials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "material_id")
    private int materialId;

    @Column(name = "material_name")
    private String materialName;


}
