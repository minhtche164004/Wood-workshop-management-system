package com.example.demo.Dto.ProductDTO;

import com.example.demo.Entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestProductDTO_Show {
    private int re_productId;
    private String re_productName;
    private String description;
    private Integer quantity;
    private BigDecimal price;
    private Date completionTime;
    private Status_Product status;
    private List<Product_Requestimages> imageList;
    private List<String> sub_material_name;
//    private Requests requests;
    private String code;
    private int request_id;

}
