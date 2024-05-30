package com.example.demo.Repository;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    List<Categories> findAll();
    @Query("SELECT u FROM Categories u WHERE u.categoryId = :query")
    Categories findById(int query);
}