package com.example.demo.Repository;

import com.example.demo.Entity.RequestProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestProductRepository extends JpaRepository<RequestProducts,Integer> {
}
