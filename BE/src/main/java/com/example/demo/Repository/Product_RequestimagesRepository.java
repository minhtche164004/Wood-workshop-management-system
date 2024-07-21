package com.example.demo.Repository;

import com.example.demo.Entity.Product_Requestimages;
import com.example.demo.Entity.Productimages;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface Product_RequestimagesRepository extends JpaRepository<Product_Requestimages,Integer> {

    @Query("SELECT pri FROM Product_Requestimages pri WHERE pri.requestProducts.requestProductId = :requestProductId")
    List<Product_Requestimages> findById(@Param("requestProductId") int requestProductId);


    @Query("SELECT p FROM Product_Requestimages p WHERE p.requestProducts.requestProductId = :requestProductId")
    List<Product_Requestimages> findImageByProductId(@Param("requestProductId") int requestProductId);

    @Query("SELECT p.fullPath FROM Product_Requestimages p JOIN p.requestProducts r WHERE r.requestProductId = :requestProductId")
    Optional<String> findFirstFullPathImageByProductId(@Param("requestProductId") int requestProductId);


    @Query(value = "SELECT product_request_images.full_path FROM product_request_images JOIN request_products ON product_request_images.request_product_id = request_products.request_product_id WHERE request_products.request_product_id = :requestProductId ORDER BY request_products.request_product_id DESC LIMIT 1", nativeQuery = true)
   String findFirstFullPathImageByProductTest(@Param("requestProductId") int requestProductId);


    @Query("SELECT p.fullPath FROM Product_Requestimages p JOIN p.requestProducts r WHERE r.requestProductId = :requestProductId")
    List<String> findFullPathImageByProductId(@Param("requestProductId") int requestProductId);


    List<Product_Requestimages> findByRequestProducts_RequestProductId(int requestProductId);


    @Transactional
    @Modifying
    @Query("DELETE FROM Product_Requestimages u WHERE u.productImageId = :query")
    void deleteRequestProductimagesById(@Param("query") int productImageId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Product_Requestimages p WHERE p.requestProducts.requestProductId = :requestProductId")
    void deleteRequestProductImages(@Param("requestProductId") int requestProductId);



}
