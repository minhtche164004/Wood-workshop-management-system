package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_request_images", schema = "test1", catalog = "")
public class Product_Requestimages {
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
    @JoinColumn(name = "request_product_id")
    private RequestProducts requestProducts;
}
