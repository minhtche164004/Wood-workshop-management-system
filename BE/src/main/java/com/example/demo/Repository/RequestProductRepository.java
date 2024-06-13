package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestProductRepository extends JpaRepository<RequestProducts,Integer> {
    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductName = :query")
    RequestProducts findByName(String query);

    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    RequestProducts findById(int query);

    int countByRequestProductName(String RequestProductName);
}
