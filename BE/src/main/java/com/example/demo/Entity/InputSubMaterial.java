package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "input_subMaterial", schema = "test1", catalog = "")
public class InputSubMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_id")
    private Integer input_id;

    @ManyToOne
    @JoinColumn(name = "sub_material_id") // Khóa ngoại tới bảng materials
//    @JsonIgnore // Không đưa vào JSON để tránh vòng lặp
    private SubMaterials subMaterials; // Đổi tên thuộc tính thành "material"

    @Column(name = "quantity")
    private double quantity;

    @Column(name = "unit_price") //giá nhập
    private BigDecimal unit_price;

    @Column(name = "out_price") //giá bán
    private BigDecimal out_price;

    @Column(name = "date_input")
    private Date date_input;

    @ManyToOne
    @JoinColumn(name = "action_type_id")
    private Action_Type actionType;


}
