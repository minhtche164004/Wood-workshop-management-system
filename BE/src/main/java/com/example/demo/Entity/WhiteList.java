package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "whitelist")
@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WhiteList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // ID của bản ghi trong bảng whitelist

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Liên kết với bảng User

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Products product; // Liên kết với bảng Products

    public WhiteList(User user, Products product) {
        this.user = user;
        this.product = product;
    }
}
