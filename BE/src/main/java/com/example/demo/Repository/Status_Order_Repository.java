package com.example.demo.Repository;

import com.example.demo.Entity.Status_Order;
import com.example.demo.Entity.Status_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface Status_Order_Repository extends JpaRepository<Status_Order,Integer> {
    @Query("SELECT u FROM Status_Order u WHERE u.status_id = :query")
    Status_Order findById(int query);



}
