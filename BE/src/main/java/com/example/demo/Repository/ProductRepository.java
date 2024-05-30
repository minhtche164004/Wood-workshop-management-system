package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Products, Integer>  {
    List<Products> findAll();

    @Query("SELECT COUNT(p) FROM Products p WHERE p.completionTime = :date") // đếm số product trong ngày
    int countByCreatedAtDate(@Param("date") LocalDate date);
}
