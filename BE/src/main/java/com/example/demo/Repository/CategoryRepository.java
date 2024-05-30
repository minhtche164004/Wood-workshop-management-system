package com.example.demo.Repository;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    List<Categories> findAll();
}