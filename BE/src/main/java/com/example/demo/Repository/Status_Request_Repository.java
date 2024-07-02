package com.example.demo.Repository;

import com.example.demo.Entity.Status_Request;
import com.example.demo.Entity.Status_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Status_Request_Repository extends JpaRepository<Status_Request,Integer> {
    @Query("SELECT u FROM Status_Request u")
    List<Status_Request> getAllStatus();

    @Query("SELECT u FROM Status_Request u WHERE u.status_id = :status_id")
    Status_Request getById(int status_id);
}
