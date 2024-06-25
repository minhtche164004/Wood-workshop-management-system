//package com.example.demo.Entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "jobs", schema = "test1", catalog = "")
//public class Job_Log {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "job_id")
//    private int jobId;
//
//    //thêm trường name cho job
//    @Column(name = "job_name")
//    private String job_name;
//
//    @ManyToOne
//    @JsonIgnore
//    @JoinColumn(name = "user_id")
//    private User user;  // Liên kết với entity User
//
//    @ManyToOne
//    @JoinColumn(name = "product_id")
//    @JsonIgnore
//    private Products product;  // Liên kết với entity Products
//
//    @ManyToOne
//    @JoinColumn(name = "request_product_id")
//    @JsonIgnore
//    private RequestProducts requestProducts;  // Liên kết với entity RequestProduct
//
//    @ManyToOne
//    @JoinColumn(name = "order_detail_id") // Khóa ngoại tới OrderDetail
//    private Orderdetails orderdetails;
//
//    //nay la status của job (status_product có type=1 , là đang thi công hay đã hoàn thành)
//    @ManyToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
////    @JsonIgnore
//    @JoinColumn(name = "status_id")
//    private Status_Job status;
//
//
//    @Column(name = "description")
//    private String description;
//
//    @Column(name = "time_finish")
//    private Date timeFinish;
//
//    @Column(name = "quantity_product")
//    private Integer quantityProduct;
//
//    @Column(name = "cost")
//    private BigDecimal cost;
//
//    @Column(name = "time_start")
//    private Date timeStart;
//
//    @Column(name = "code")
//    private String code;
//}
