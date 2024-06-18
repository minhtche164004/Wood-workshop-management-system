package com.example.demo.Repository;

import com.example.demo.Entity.Product_Requestimages;
import com.example.demo.Entity.Requestimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface RequestimagesRepository extends JpaRepository<Requestimages,Integer> {

    @Query("SELECT pri FROM Requestimages pri WHERE pri.requests.requestId = :requestId")
    List<Requestimages> findById(@Param("requestId") int requestId);

}
