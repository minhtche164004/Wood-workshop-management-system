package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "request_products_submaterials", schema = "test1", catalog = "")
public class RequestProductsSubmaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "request_products_submaterials_id")
    private int requestProductsSubmaterialsId;

    @Column(name = "request_product_id")
    private Integer requestProductId;

//    @Column(name = "sub_material_id")
//    private Integer subMaterialId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_material_id")
    private SubMaterials subMaterial;





}
