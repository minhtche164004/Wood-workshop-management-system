package com.example.demo.Repository;

import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.Processproducterror;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessproducterrorRepository extends JpaRepository<Processproducterror,Integer> {

    @Query("SELECT u FROM Processproducterror u WHERE u.product.productId = :query")
    List<Processproducterror> getProcessproducterrorByProductId(int query);

    @Query("SELECT u FROM Processproducterror u WHERE u.processProductErrorId = :query")
    Processproducterror FindByIdProductErrorId(int query);
}
