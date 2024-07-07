package com.example.demo.Dto.RequestDTO;

import com.example.demo.Entity.Product_Requestimages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestProductEditDTO {
//    private Integer id;
    private String requestProductName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Date completionTime;
    private int status_id;
    private List<Product_Requestimages> imagesList;
}
