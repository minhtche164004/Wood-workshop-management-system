package com.example.demo.Dto.SubMaterialDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDistribution {
    private Integer id;
    private String sub_material_name;
    private String order_code;
    private String employee_name;    // tên nhân viên nhận nguyên vật liệu
    private double quantity;  //số lượng vật liệu cung cấp cho nhân viên
    private Date delivery_date ; //ngày nhận
    private String category_name; //loại sản phẩm


}
