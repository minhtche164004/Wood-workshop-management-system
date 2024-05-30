package com.example.demo.Repository;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
    @Query("SELECT u FROM Status u WHERE u.status_name = :query")
    Status findByName(String query);

    @Query("SELECT u FROM Status u WHERE u.status_id = :query")
    Status findById(int query);

}
