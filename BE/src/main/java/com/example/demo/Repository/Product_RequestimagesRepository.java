package com.example.demo.Repository;

import com.example.demo.Entity.Product_Requestimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface Product_RequestimagesRepository extends JpaRepository<Product_Requestimages,Integer> {
}
