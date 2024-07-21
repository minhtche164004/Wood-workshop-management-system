package com.example.demo.Dto.ProductDTO;

import com.example.demo.Entity.Product_Requestimages;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Data
public class RequestProductAllDTO {
    private int re_productId;
    private String re_productName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private Date completionTime;
    private Integer status_id;
    private String status_name;
    private Integer productImageId;
    private String fullPath;
    private String code;
    private Integer request_id;

    public RequestProductAllDTO(int re_productId, String re_productName, String description, BigDecimal price, Integer quantity, Date completionTime,
                                Integer status_id, String status_name, Integer productImageId, String fullPath, String code, Integer request_id) {
        this.re_productId = re_productId;
        this.re_productName = re_productName;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.completionTime = completionTime;
        this.status_id = status_id;
        this.status_name = status_name;
        this.productImageId = productImageId;
        this.fullPath = fullPath;
        this.code = code;
        this.request_id = request_id;
    }

//    public RequestProductAllDTO(Integer re_productId, String re_productName, String description, BigDecimal price,
//                                Integer quantity, Date completionTime, Integer status_id, String status_name, Integer productImageId, String code, Integer request_id) {
//        this.re_productId = re_productId;
//        this.re_productName = re_productName;
//        this.description = description;
//        this.price = price;
//        this.quantity = quantity;
//        this.completionTime = completionTime;
//        this.status_id = status_id;
//        this.status_name = status_name;
//        this.productImageId = productImageId;
//        this.code = code;
//        this.request_id = request_id;
//    }
}
