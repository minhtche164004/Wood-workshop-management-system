package com.example.demo.Repository;

import com.example.demo.Dto.ProductDTO.RequestProductAllDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface RequestProductRepository extends JpaRepository<RequestProducts,Integer> {
    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductName = :query")
    RequestProducts findByName(String query);

    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    RequestProducts findById(int query);

//    @Query("SELECT new com.example.demo.Dto.ProductDTO.RequestProductAllDTO(rp.requestProductId, rp.requestProductName, rp.description, " +
//            "rp.price, rp.quantity, rp.completionTime, rp.requests.requestId, pri.fullPath, pri.productImageId) " +
//            "FROM RequestProducts rp JOIN rp.productRequestimages pri")
//    List<RequestProductAllDTO> findAllWithImages();


    @Query("SELECT u FROM RequestProducts u WHERE u.requestProductId = :query")
    Optional<RequestProducts> findByIdJob(int query);


    @Query("SELECT p FROM RequestProducts p WHERE " +
            "(p.requestProductName LIKE %:search% OR :search IS NULL) AND " +
            "(p.status.status_id = :statusId OR :statusId IS NULL) AND " +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
            "(p.price <= :maxPrice OR :maxPrice IS NULL)")
    List<RequestProducts> filterRequestProductsForAdmin(@Param("search") String search,
                                          @Param("statusId") Integer statusId,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice);


    @Query("SELECT p FROM RequestProducts p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<RequestProducts> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);









    int countByRequestProductName(String RequestProductName);
}
