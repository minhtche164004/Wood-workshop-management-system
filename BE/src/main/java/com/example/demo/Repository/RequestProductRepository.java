package com.example.demo.Repository;

import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestProductRepository extends JpaRepository<RequestProducts,Integer> {
    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductName = :query")
    RequestProducts findByName(String query);

    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    RequestProducts findById(int query);
    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    RequestProducts findByIdInteger(Integer query);

    @Query("SELECT u FROM RequestProducts u WHERE u.orders.orderId = :query")
    List<RequestProducts> findByRequestId(int query);

    @Transactional
    @Modifying
    @Query("DELETE FROM RequestProducts u WHERE u.requestProductId = :query")
    void deleteByRequestProductId(@Param("query") int requestProductId);

    @Transactional
    @Modifying
    @Query("update RequestProducts u set u.requestProductName = ?2,u.description=?3,u.price=?4,u.status.status_id=?5," +
            "u.quantity=?6,u.completionTime=?7 where u.requestProductId = ?1")
    void updateRequestProduct(int requestProductId, String productName, String description, BigDecimal price, int status_id,int quantity, Date completionTime);



//    @Query("SELECT new com.example.demo.Dto.ProductDTO.RequestProductAllDTO(rp.requestProductId, rp.requestProductName, rp.description, " +
//            "rp.price, rp.quantity, rp.completionTime, rp.requests.requestId, pri.fullPath, pri.productImageId) " +
//            "FROM RequestProducts rp JOIN rp.productRequestimages pri")
//    List<RequestProductAllDTO> findAllWithImages();


    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    Optional<RequestProducts> findByIdJob(int query);

    @Query("SELECT u FROM RequestProducts u WHERE u.orders.userInfor.user.userId = :query")
    List<RequestProducts> findByUserId(int query);


    @Query("SELECT p FROM RequestProducts p WHERE " +
            "(p.requestProductName LIKE %:search% OR :search IS NULL) AND " +
            "(p.status.status_id = :status_id OR :status_id IS NULL) AND " +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
            "(p.price <= :maxPrice OR :maxPrice IS NULL)")
    List<RequestProducts> filterRequestProductsForAdmin(@Param("search") String search,
                                          @Param("status_id") Integer status_id,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice);




    @Query("SELECT p FROM RequestProducts p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<RequestProducts> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);









    int countByRequestProductName(String RequestProductName);
}
