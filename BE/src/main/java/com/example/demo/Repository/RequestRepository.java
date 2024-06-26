package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;
import com.example.demo.Entity.Requests;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

@Repository
public interface RequestRepository extends JpaRepository<Requests,Integer> {

    @Query(value = "SELECT p.* FROM requests p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Requests findRequestTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
    Requests findById(int query);

    @Transactional
    @Modifying
    @Query("UPDATE Requests u SET u.status.status_id = :status_id WHERE u.requestId = :requestId")
    void updateStatus(@Param("requestId") int requestId, @Param("status_id") int status_id);

    @Transactional
    @Modifying
    @Query("update Requests u set u.description = ?2, u.requestDate=?3 where u.requestId = ?1")
    void updateRequest(int requestId,String description,Date requestDate);


//    @Transactional
//    @Modifying
//    @Query("update Requests u set u.requestDate = ?2,u.status.status_id=?3,u.response=?4,u.description=?5,u.address=?6," +
//            "u.fullname=?7,u.phoneNumber=?8,u.city_province=?9,u.district=?10, u.wards=?11 where u.requestId = ?1")
//    void updateRequest(int productId, Date requestDate, int status_id, String response, String description, String address,
//                       String fullname, String phoneNumber, String city_province, String district, String wards);

}
