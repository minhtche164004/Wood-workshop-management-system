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


    @ManyToOne
    @JoinColumn(name = "material_id") // Khóa ngoại tới bảng materials
    @JsonIgnore // Không đưa vào JSON để tránh vòng lặp
    private Materials material; // Đổi tên thuộc tính thành "material"

}

//@ManyToOne: Mỗi sub-material thuộc về một material.
//@JoinColumn(name = "material_id") chỉ ra rằng mối quan hệ này được xác định bởi khóa ngoại material_id trong bảng sub_materials.
