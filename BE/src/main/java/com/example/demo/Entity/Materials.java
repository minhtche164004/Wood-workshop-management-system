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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "material_id")
    private int materialId;

    @Column(name = "material_name")
    private String materialName;

    @JsonIgnore
    @OneToMany(mappedBy = "materials", fetch = FetchType.LAZY)
    private List<SubMaterials> subMaterials;


//    @JsonIgnore
//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<User> user;

    public Materials(String materialName) {
        this.materialName = materialName;
    }
}
