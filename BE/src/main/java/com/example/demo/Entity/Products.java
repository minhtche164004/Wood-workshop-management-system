package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products", schema = "test1", catalog = "")
public class Products {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private int productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "image")
    private String image;

    @Column(name = "completion_time")
    @Temporal(TemporalType.DATE)
    private Date completionTime;

    @Column(name = "enddate_warranty")
    @Temporal(TemporalType.DATE)
    private Date enddateWarranty;

    @Column(name = "code")
    private String code;

    @Column(name = "type") //type này là kiểu có sẵn hoặc không có sẵn( o là không có sẵn,1 là hàng có sẵn )
    private int type;

    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
//    @JsonIgnore
    @JoinColumn(name = "status_id")
    private Status_Product status;

    @ManyToOne(fetch = FetchType.EAGER)
//    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories categories;


    public Products(String productName, String description, Integer quantity, BigDecimal price, String image, Date completionTime, Date enddateWarranty, String code, int type, Status_Product status, Categories categories) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.image = image;
        this.completionTime = completionTime;
        this.enddateWarranty = enddateWarranty;
        this.code = code;
        this.type = type;
        this.status = status;
        this.categories = categories;
    }
}


/*
@OneToMany(mappedBy = "product", cascade = CascadeType.ALL) private List<Jobs> jobs;:
Thể hiện mối quan hệ một-nhiều với Jobs. Một products có thể được tạo ra từ nhiều job.*/
