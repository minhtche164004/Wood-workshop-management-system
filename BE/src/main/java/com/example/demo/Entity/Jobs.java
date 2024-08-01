package com.example.demo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "jobs", schema = "test1", catalog = "")
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_id")
    private int jobId;

    //thêm trường name cho job
    @Column(name = "job_name")
    private String job_name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;  // Liên kết với entity User

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Products product;  // Liên kết với entity Products

    @ManyToOne
    @JoinColumn(name = "request_product_id")
    @JsonIgnore
    private RequestProducts requestProducts;  // Liên kết với entity RequestProduct

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "order_detail_id") // Khóa ngoại tới OrderDetail
    private Orderdetails orderdetails;

//nay la status của job (status_product có type=1 , là đang thi công hay đã hoàn thành)
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "status_id")
    private Status_Job status;


    @Column(name = "description")
    private String description;

    @Column(name = "time_finish")
    private Date timeFinish;

    @Column(name = "quantity_product")
    private Integer quantityProduct;

    @Column(name = "cost")
    private BigDecimal cost;

    @Column(name = "time_start")
    private Date timeStart;

    @Column(name = "code")
    private String code;

    @Column(name = "job_log")
    private boolean job_log;

    @Column(name = "reassigned") //được phân công lại
    private boolean reassigned;

    @Column(name = "original_quantity_product")
    private Integer originalQuantityProduct;

//    @OneToMany(mappedBy = "process_product_error")
//    @JsonIgnore
//    private List<Processproducterror> processproducterrors;

    @OneToMany(mappedBy = "job") // Thêm mối quan hệ với Processproducterror
    @JsonIgnore // Tùy chọn: bỏ qua trường này khi serialize thành JSON
    private List<Processproducterror> processProductErrors;

}


/*@ManyToOne @JoinColumn(name = "user_id") private User user;: Thể hiện mối quan hệ nhiều-một với User. Một job được thực hiện bởi một user.
@ManyToOne @JoinColumn(name = "product_id") private Products product;: Thể hiện mối quan hệ nhiều-một với Products. Một job tạo ra một sản phẩm.*/
