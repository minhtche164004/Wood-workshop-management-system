package com.example.demo.Repository;

import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {

    @Query(value = "SELECT p.* FROM orders p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Orders findOrderTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Orders u WHERE u.code = :query")
    Orders findByCode(String query);

    @Query("SELECT u FROM Orders u  WHERE u.address LIKE CONCAT('%', :keyword, '%') OR " +
            "u.code LIKE CONCAT('%', :keyword, '%')")
    List<Orders> findOrderByAddressorCode(@Param("keyword") String keyword);

    @Query("SELECT o FROM Orders o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Orders> findByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT u FROM Orders u WHERE u.status.status_id = :query")
    List<Orders> filterByStatus(int query);

    @Query("SELECT u FROM Orders u WHERE u.userInfor.user.userId = :query")
    List<Orders> findHistoryOrder(int query);






}
