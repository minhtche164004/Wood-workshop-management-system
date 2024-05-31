package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_sub_materials", schema = "test1", catalog = "")
public class ProductSubMaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_sub_material_id")
    private int productSubMaterialId;

//    @Column(name = "sub_material_id")
//    private Integer subMaterialId;

//    @Column(name = "product_id")
//    private Integer productId;


    @ManyToOne(fetch = FetchType.LAZY) // Sử dụng FetchType.LAZY để tối ưu việc truy vấn
    @JsonIgnore
    @JoinColumn(name = "sub_material_id") // Chỉ định cột khóa ngoại
    private SubMaterials subMaterial;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "product_id") // Chỉ định cột khóa ngoại
    private Products product; // Thêm thuộc tính Products để liên kết với bảng products
}


