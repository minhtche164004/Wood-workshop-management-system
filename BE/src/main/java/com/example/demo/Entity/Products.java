package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Objects;
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

    @Column(name = "completion_time")
    @Temporal(TemporalType.DATE)
    private Date completionTime;

    @Column(name = "image")
    private String image;

    @Column(name = "enddate_warranty")
    @Temporal(TemporalType.DATE)
    private Date enddateWarranty;

    @Column(name = "code")
    private String code;
    @Column(name = "type")
    private int type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @JoinColumn(name = "status_id")
    private Status status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "category_id")
    private Categories categories;


    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY)
    @JsonBackReference
    private List<ProductSubMaterials> productSubMaterials;

    public Products(String productName, String description, Integer quantity, BigDecimal price, Date completionTime, String image, Date enddateWarranty, String code, Status status, Categories categories,Integer type) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.completionTime = completionTime;
        this.image = image;
        this.enddateWarranty = enddateWarranty;
        this.code = code;
        this.status = status;
        this.categories = categories;
        this.type=type;
    }
}
