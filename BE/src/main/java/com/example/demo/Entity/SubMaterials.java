package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sub_materials", schema = "test1", catalog = "")
public class SubMaterials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sub_material_id")
    private int subMaterialId;

    @Column(name = "sub_material_name")
    private String subMaterialName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "code")
    private String code;


    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "material_id")
    private Materials materials;


//    @JsonIgnore
//    @OneToMany(mappedBy = "materials", cascade = CascadeType.ALL)
//    @JsonBackReference
//    private List<SubMaterials> subMaterials;

    @OneToMany(mappedBy = "subMaterial", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductSubMaterials> productSubMaterials ;

    @OneToMany(mappedBy = "subMaterial",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RequestProductsSubmaterials> requestProductsSubmaterials ;

    @OneToMany(mappedBy = "subMaterial",fetch = FetchType.LAZY) // Một nguyên liệu phụ có thể được cung cấp bởi nhiều nhà cung cấp
    @JsonIgnore // Tránh vòng lặp vô hạn khi chuyển đổi sang JSON
    private List<Suppliermaterial> suppliers;

    public SubMaterials(String subMaterialName, String description, Integer quantity, BigDecimal unitPrice, String code, Materials materials, List<ProductSubMaterials> productSubMaterials, List<RequestProductsSubmaterials> requestProductsSubmaterials, List<Suppliermaterial> suppliers) {
        this.subMaterialName = subMaterialName;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.code = code;
        this.materials = materials;
        this.productSubMaterials = productSubMaterials;
        this.requestProductsSubmaterials = requestProductsSubmaterials;
        this.suppliers = suppliers;
    }
}
