package com.example.demo.Repository;

import com.example.demo.Entity.Product_Requestimages;
import com.example.demo.Entity.Productimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface Product_RequestimagesRepository extends JpaRepository<Product_Requestimages,Integer> {

    @Query("SELECT pri FROM Product_Requestimages pri WHERE pri.requestProducts.requestProductId = :requestProductId")
    List<Product_Requestimages> findById(@Param("requestProductId") int requestProductId);


    @Query("SELECT p FROM Product_Requestimages p WHERE p.requestProducts.requestProductId = :requestProductId")
    List<Product_Requestimages> findImageByProductId(@Param("requestProductId") int requestProductId);



}
