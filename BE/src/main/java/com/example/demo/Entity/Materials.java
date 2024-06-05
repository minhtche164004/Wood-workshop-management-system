package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "materials", schema = "test1", catalog = "")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Materials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private int materialId;

    @Column(name = "material_name")
    private String materialName;

//    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<SubMaterials> subMaterials;

//    @ManyToMany(mappedBy = "materials")
//    @JsonIgnore
//    private List<User> employees;

    public Materials(String materialName) {
        this.materialName = materialName;
    }
}
