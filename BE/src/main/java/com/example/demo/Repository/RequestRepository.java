package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.RequestDTO.RequestEditCusDTO;
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
import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Requests,Integer> {

    @Query(value = "SELECT p.* FROM requests p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Requests findRequestTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
    Requests findById(int query);


    @Query("SELECT u FROM Requests u WHERE u.status.status_id = 1")
    List<Requests> findAllRequest();

    @Query("SELECT u FROM Requests u WHERE u.status.status_id = 2")
    List<Requests> findAllRequestAccept();

//    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
//    Requests findByRequestProductId(int query);



    @Query("SELECT u FROM Requests u WHERE u.user.userId = :query AND u.status.status_id = 2")
    List<Requests> findByUserId(int query);

    @Transactional
    @Modifying
    @Query("UPDATE Requests u SET u.status.status_id = :status_id WHERE u.requestId = :requestId")
    void updateStatus(@Param("requestId") int requestId, @Param("status_id") int status_id);

    @Transactional
    @Modifying
    @Query("update Requests u set u.description = ?2 where u.requestId = ?1")
    void updateRequest(int requestId,String description);

//    @Query("SELECT new com.example.demo.Dto.RequestDTO.RequestEditCusDTO(r.description, " +
//            "(SELECT ri.fullPath FROM Requestimages ri WHERE ri.requests = r)) " +
//            "FROM Requests r " +
//            "WHERE r.requestId = :requestId")
//    RequestEditCusDTO getRequestEditCusDTOById(int requestId);



//    @Transactional
//    @Modifying
//    @Query("update Requests u set u.requestDate = ?2,u.status.status_id=?3,u.response=?4,u.description=?5,u.address=?6," +
//            "u.fullname=?7,u.phoneNumber=?8,u.city_province=?9,u.district=?10, u.wards=?11 where u.requestId = ?1")
//    void updateRequest(int productId, Date requestDate, int status_id, String response, String description, String address,
//                       String fullname, String phoneNumber, String city_province, String district, String wards);

}
