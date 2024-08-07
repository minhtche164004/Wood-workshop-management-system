package com.example.demo.Repository;

import com.example.demo.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, Integer> {
    List<Categories> findAll();
    @Query("SELECT u FROM Categories u WHERE u.categoryId = :query")
    Categories findById(int query);
    Categories findByCategoryName(String query);
    @Query("SELECT p FROM Products p LEFT JOIN p.categories c  WHERE c.categoryId = :categoryId ")
    List<Products> findProductByCategoryId(int categoryId);

    @Query("SELECT u FROM Categories u  WHERE u.categoryName LIKE CONCAT('%', :keyword, '%')")
    List<Categories> findCategoriesByName(@Param("keyword") String keyword);


//    @Query("SELECT u FROM Orderdetails u WHERE u.product.productId = :query")
//    List<Orderdetails> getOrderDetailByProductId(int query);


}