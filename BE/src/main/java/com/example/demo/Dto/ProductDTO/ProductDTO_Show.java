package com.example.demo.Dto.ProductDTO;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Productimages;
import com.example.demo.Entity.Status_Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO_Show {
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
    private List<String> sub_material_name;

}
