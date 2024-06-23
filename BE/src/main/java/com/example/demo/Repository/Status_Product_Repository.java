package com.example.demo.Repository;

import com.example.demo.Entity.Status_Product;
import com.example.demo.Entity.Status_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface Status_Product_Repository extends JpaRepository<Status_Product, Integer> {
    @Query("SELECT u FROM Status_Product u WHERE u.status_name = :query")
    Status_Product findByName(String query);

    @Query("SELECT u FROM Status_Product u WHERE u.status_id = :query")
    Status_Product findById(int query);

    @Query("SELECT u FROM Status_Product u WHERE u.type=0")
    List<Status_Product> GetListStatusType0();

    @Query("SELECT u FROM Status_Product u WHERE u.type=1")
    List<Status_Product> GetListStatusType1();

    @Modifying
    @Query("UPDATE Status_Product u SET u.status_name = :newName WHERE u.status_id = :id")
    void updateStatusName(@Param("newName") String newName, @Param("id") int id);


}
