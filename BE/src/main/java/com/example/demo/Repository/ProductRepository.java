package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findAll();
    @Query(value = "SELECT p.* FROM products p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Products findProductTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Products u WHERE u.productId = :query")
    Products findById(int query);

    @Query("SELECT u FROM Products u WHERE u.categories.categoryId = :query")
    List<Products> findByCategory(int query);

    @Query("SELECT u FROM Products u WHERE u.productName = :query")
    Products findByName(String query);

    @Query("SELECT u FROM Products u  WHERE u.productName LIKE CONCAT('%', :keyword, '%') OR " +
            "u.code LIKE CONCAT('%', :keyword, '%')")
    List<Products> findProductByNameCode(@Param("keyword") String keyword);


    int countByProductName(String ProductName);

    @Transactional
    @Modifying
    @Query("update Products u set u.productName = ?2,u.description=?3,u.quantity=?4,u.price=?5,u.status.status_id=?6," +
            "u.categories.categoryId=?7,u.type=?8,u.image=?9,u.completionTime=?10, u.enddateWarranty=?11 where u.productId = ?1")
    void updateProduct(int productId, String productName, String description, int quantity, BigDecimal price, int status_id, int categoryId, int type, String image, Date completionTime,Date enddateWarranty);
}

