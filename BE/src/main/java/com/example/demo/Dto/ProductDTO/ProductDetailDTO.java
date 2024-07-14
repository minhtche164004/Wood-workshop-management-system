package com.example.demo.Dto.ProductDTO;

import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Productimages;
import com.example.demo.Entity.Status_Product;
import com.example.demo.Entity.SubMaterials;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDTO {
    private int productId;
    private String productName;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private String image;
    private Date completionTime;
    private Date enddateWarranty;
    private String code;
    private int type;
    private Status_Product status;
    private Categories categories;
    private List<Productimages> imageList;
    private List<SubMaterialViewDTO> sub_material_name;

}
