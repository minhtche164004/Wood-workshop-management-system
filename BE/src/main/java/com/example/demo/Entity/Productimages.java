package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productimages", schema = "test1", catalog = "")
@Builder
public class Productimages implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_image_id")
    private int productImageId;

    @Column(name = "image")
    private String image_name;


    @Column(name="file_original_name")
    private String fileOriginalName;

    @Column(name="extension_name")
    private String extension_name;

    @Column(name="full_path")
    private String fullPath;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Products product;

}
