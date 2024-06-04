package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productimages", schema = "test1", catalog = "")
@Builder
public class Productimages {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_image_id")
    private int productImageId;


    @Column(name = "image")
    private String image_name;
    @Column(name = "type")
    private String type;


    @Column(name = "imagedata",length = 1000)
    private byte[] imageData;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "product_id")
    private Products product;

}
