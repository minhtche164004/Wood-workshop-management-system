//package com.example.demo.Entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//
//@Entity
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Table(name = "batch", schema = "test1", catalog = "")
//public class Batch {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "batch_id")
//    private Integer batchId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sub_material_id")
//    @JsonIgnore
//    private SubMaterials subMaterial;
//
//   // Liên kết đến lần nhập hàng cụ thể tạo ra lô hàng này (nếu cần).
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "input_sub_material_id")
//    private InputSubMaterial inputSubMaterial;
//    //Ngày tạo lô hàng
//    @Column(name = "created_at")
//    private Date createdAt;
//
//    //Số lượng sản phẩm trong lô hàng.
//    @Column(name = "quantity")
//    private Integer quantity;
//
//    //Ghi chú thêm về lô hàng.
//    @Column(name = "notes")
//    private String notes;
//
//    //mã lô hàng
//    @Column(name = "code")
//    private String code;
//}
