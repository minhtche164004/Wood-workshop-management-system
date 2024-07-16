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

    @ManyToOne()
    @JoinColumn(name = "sub_material_id", referencedColumnName = "sub_material_id") // Chỉ rõ tên cột liên kết ở cả 2 bảng
    private SubMaterials subMaterial;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Products product;

    @Column(name = "quantity")
    private Double quantity;

    @ManyToOne // Đánh dấu mối quan hệ Many-to-One
    @JoinColumn(name = "input_id", referencedColumnName = "input_id") // Liên kết với input_id của InputSubMaterial
    private InputSubMaterial inputSubMaterial; // Thêm trường để lưu InputSubMaterial


    public ProductSubMaterials(SubMaterials subMaterial, Products product, Double quantity,InputSubMaterial inputSubMaterial) {
        this.subMaterial = subMaterial;
        this.product = product;
        this.quantity = quantity;
        this.inputSubMaterial=inputSubMaterial;
    }
}

/*Một nhân viên (users) có thể có nhiều bản ghi vật liệu nhân viên (employeematerials).
Một vật liệu chi tiết sản phẩm (product_sub_materials) có thể được sử dụng bởi nhiều nhân viên (employeematerials).
Bảng employeematerials đóng vai trò là cầu nối giữa users và product_sub_materials, ghi lại
thông tin về việc nhân viên nào đã nhận được bao nhiêu vật liệu chi tiết của sản phẩm nào.*/
