package com.example.demo.Repository;

import com.example.demo.Entity.Status_Job;
import com.example.demo.Entity.Status_Order;
import com.example.demo.Entity.Status_Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Status_Job_Repository extends JpaRepository<Status_Job,Integer> {
    @Query("SELECT u FROM Status_Job u WHERE u.status_id = :query")
    Status_Job findById(int query);

    @Query("SELECT u FROM Status_Job u")
    List<Status_Job> getAllStatus();
}
