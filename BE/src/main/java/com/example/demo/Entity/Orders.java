package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders", schema = "test1", catalog = "")
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private int orderId;

    @Column(name = "order_date")
    private Date orderDate;

    @ManyToOne // Relationship with Status entity
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    private Status_Order status;  // Assuming you have a Status entity

    @Column(name = "total_amount", precision = 38, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "special_order")
    private Boolean specialOrder; // Using Boolean for clarity

    @Column(name = "payment_method")
    private Integer paymentMethod; // Assuming you'll use an enum later

    @Column(name = "deposite")
    private BigDecimal deposite;

    @ManyToOne // Relationship with InformationUser entity
    @JoinColumn(name = "infor_id", referencedColumnName = "infor_id")
    private UserInfor userInfor; // Assuming you have an InformationUser entity



    @Column(name = "code")
    private String code;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "address")
    private String address;

    @Column(name = "city_province")
    private String city_province;

    @Column(name = "district")
    private String district;

    @Column(name = "wards")
    private String wards;

    @Column(name = "order_finish")
    private Date orderFinish;

    @Column(name = "response") //tương tự như bên class request
    private String response;

    @Column(name = "description") //tương tự như bên class request
    private String description;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "refund")
    private BigDecimal refund;

    @Column(name = "contract_date") //ngày đơn hàng thi công xong , bắt theo cái date cảu sản phẩm muộn nhất có trong đơn hàng(dành cho đơn hàng đặc biệt)
    private Date contractDate;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<Requestimages> requestImages;

    @ManyToOne // Relationship with Status entity
    @JoinColumn(name = "refund_id", referencedColumnName = "refund_id")
    private RefundStatus refundStatus;  // Assuming you have a Status entity


}
