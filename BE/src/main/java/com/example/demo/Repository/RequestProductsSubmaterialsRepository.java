package com.example.demo.Repository;

import com.example.demo.Entity.RequestProductsSubmaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestProductsSubmaterialsRepository extends JpaRepository<RequestProductsSubmaterials,Integer> {
}
