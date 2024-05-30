package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer>  {
    List<Products> findAll();
}
