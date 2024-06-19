package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.WhiteList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteListRepository extends JpaRepository<WhiteList,Integer> {

    @Query("SELECT u FROM WhiteList u WHERE u.user.userId = :query")
    List<WhiteList> findByUserID(int query);

}
