package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;
import com.example.demo.Entity.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Requests,Integer> {

    @Query(value = "SELECT p.* FROM requests p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Requests findRequestTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
    Requests findById(int query);
}
