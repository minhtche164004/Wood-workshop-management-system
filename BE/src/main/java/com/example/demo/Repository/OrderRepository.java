package com.example.demo.Repository;

import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {

    @Query(value = "SELECT p.* FROM orders p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Orders findOrderTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Orders u WHERE u.code = :query")
    Orders findByCode(String query);
}
