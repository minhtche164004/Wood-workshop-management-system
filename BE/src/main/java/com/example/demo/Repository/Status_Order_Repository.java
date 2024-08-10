package com.example.demo.Repository;

import com.example.demo.Entity.RefundStatus;
import com.example.demo.Entity.Status_Order;
import com.example.demo.Entity.Status_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Status_Order_Repository extends JpaRepository<Status_Order,Integer> {
    @Query("SELECT u FROM Status_Order u WHERE u.status_id = :query")
    Status_Order findById(int query);

    @Query("SELECT u FROM Status_Order u")
    List<Status_Order> getAllStatus();


    @Query("SELECT u FROM RefundStatus u")
    List<RefundStatus> getAllRefundStatus();

    @Query("SELECT u FROM RefundStatus u WHERE u.refundId = :refundId")
    RefundStatus findByRefundStatusId(int refundId);



}
