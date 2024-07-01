package com.example.demo.Repository;

import com.example.demo.Entity.Status_User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Status_User_Repository extends JpaRepository<Status_User, Integer> {
    @Query("SELECT u FROM Status_User u WHERE u.status_name = :query")
    Status_User findByName(String query);

    @Query("SELECT u FROM Status_User u WHERE u.status_id = :query")
    Status_User findById(int query);

    @Query("SELECT u FROM Status_User u WHERE u.status_id = :query")
    Status_User findById1(int query);


    @Modifying
    @Query("UPDATE Status_User u SET u.status_name = :newName WHERE u.status_id = :id")
    void updateStatusName(@Param("newName") String newName, @Param("id") int id);



    @Query("SELECT u.status_name FROM Status_User u")
    List<String> getAllStatusNames();

    @Query("SELECT u FROM Status_User u")
    List<Status_User> getAllStatus();


}
