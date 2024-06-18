package com.example.demo.Repository;

import com.example.demo.Entity.Productimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ProductImageRepository extends JpaRepository<Productimages, Integer> {

    @Query("SELECT p FROM Productimages p WHERE p.image_name = :image_name")
    Optional<Productimages> findByImage_name(@Param("image_name") String image_name);

    @Query("SELECT p FROM Productimages p WHERE p.product.productId = :productId")
    List<Productimages> findImageByProductId(@Param("productId") int productId);


    @Query(value = "SELECT p FROM Productimages p WHERE p.product.productId = :productId" )
    List<Productimages> findByImage_Id(@Param("productId") int productId);
}
