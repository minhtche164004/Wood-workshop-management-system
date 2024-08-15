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


//    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_material_id")
    private int subMaterial;


    @ManyToOne // Many-to-One relationship with RequestProducts
    @JoinColumn(name = "request_product_id")
    private RequestProducts requestProduct;

    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne // Đánh dấu mối quan hệ Many-to-One
    @JoinColumn(name = "input_id", referencedColumnName = "input_id") // Liên kết với input_id của InputSubMaterial
    private InputSubMaterial inputSubMaterial; // Thêm trường để lưu InputSubMaterial

    public RequestProductsSubmaterials(int subMaterial, RequestProducts requestProduct, Double quantity,InputSubMaterial inputSubMaterial) {
        this.subMaterial = subMaterial;
        this.requestProduct = requestProduct;
        this.quantity = quantity;
        this.inputSubMaterial=inputSubMaterial;
    }
}
