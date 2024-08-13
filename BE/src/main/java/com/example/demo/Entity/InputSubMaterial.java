package com.example.demo.Entity;

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

    @Column(name = "quantity") //số lượng trong kho
    private double quantity;

//    @Column(name = "total_quantity")
//    private double total_quantity;

    @Column(name = "unit_price") //giá nhập
    private BigDecimal input_price;

    @Column(name = "out_price") //giá bán
    private BigDecimal out_price;

    @Column(name = "date_input") //ngày nhập vào hệ thống
    private Date date_input;

    @Column(name = "create_date") //ngày nhập kho
    private Date create_date;

    //thêm trường code(mã xuất nhập kho)
    @Column(name = "code_input")
    private String code_input;

    @ManyToOne
    @JoinColumn(name = "action_type_id")
    private Action_Type actionType;


}
