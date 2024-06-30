package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ProductRepository extends JpaRepository<Products, Integer> {
    List<Products> findAll();

    @Query(value = "SELECT p.* FROM products p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Products findProductTop(@Param("prefix") String prefix);


    @Query("SELECT u FROM Products u WHERE u.productId = :query")
    Products findById(int query);

    @Query("SELECT u FROM Products u WHERE u.code = :query")
    Products findByCode(String query);

    @Query("SELECT u FROM Products u WHERE u.categories.categoryId = :query")
    List<Products> findByCategory(int query);

    @Query("SELECT u FROM Products u WHERE u.status.status_id = :query")
    List<Products> findByStatus(int query);

//    @Query("SELECT u FROM Products u WHERE u.status.status_id = :query")
//    List<Products> ViewProductLandingPage(int query);

    @Query("SELECT u FROM Products u WHERE  u.status.status_id = 2")
    List<Products> ViewProductLandingPage();

    @Query("SELECT u FROM Products u WHERE u.productName = :query")
    Products findByName(String query);

    @Query("SELECT u FROM Products u WHERE u.productId = :query")
    Optional<Products> findByIdJob(int query);

    @Query("SELECT u FROM Products u  WHERE u.productName LIKE CONCAT('%', :keyword, '%') OR " +
            "u.code LIKE CONCAT('%', :keyword, '%')")
    List<Products> findProductByNameCode(@Param("keyword") String keyword);

    @Query("SELECT p FROM Products p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Products> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Transactional
    @Modifying
    @Query("update Products u set u.status.status_id = ?2" +
            " where u.productId = ?1")
    void updateStatus(int productId, int status_id);

    int countByProductName(String ProductName);

    @Transactional
    @Modifying
    @Query("update Products u set u.productName = ?2,u.description=?3,u.price=?4,u.status.status_id=?5," +
            "u.categories.categoryId=?6,u.type=?7,u.image=?8,u.enddateWarranty=?9 where u.productId = ?1")
    void updateProduct(int productId, String productName, String description, BigDecimal price,int status_id, int categoryId, int type, String image,Date enddateWarranty);


//    @Query("SELECT p FROM Products p WHERE " +
//            "(p.productName LIKE %:search% OR :search IS NULL) AND " +
//            "(p.categories.categoryId = :categoryIds OR :categoryId IS NULL) AND " +
//            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
//            "(p.price <= :maxPrice OR :maxPrice IS NULL) AND  p.status.status_id = 2 ")
//    List<Products> filterProductsForCus(@Param("search") String search,
//                                        @Param("categoryIds") List<Integer> categoryIds,
//                                        @Param("minPrice") BigDecimal minPrice,
//                                        @Param("maxPrice") BigDecimal maxPrice);
    @Query("SELECT p FROM Products p WHERE " +
       "(p.productName LIKE %:search% OR :search IS NULL) AND " +
       "(p.categories.categoryId IN :categoryIds OR :categoryIds IS NULL) AND " +
       "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
       "(p.price <= :maxPrice OR :maxPrice IS NULL) AND  p.status.status_id = 2 ")
List<Products> filterProductsForCus(@Param("search") String search,
                                    @Param("categoryIds") List<Integer> categoryIds,
                                    @Param("minPrice") BigDecimal minPrice,
                                    @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Products p WHERE " +
            "(p.productName LIKE %:search% OR :search IS NULL) AND " +
            "(p.categories.categoryId = :categoryId OR :categoryId IS NULL) AND " +
            "(p.status.status_id = :statusId OR :statusId IS NULL) AND " +
            "(p.price >= :minPrice OR :minPrice IS NULL) AND " +
            "(p.price <= :maxPrice OR :maxPrice IS NULL)")
    List<Products> filterProductsForAdmin(@Param("search") String search,
                                          @Param("categoryId") Integer categoryId,
                                          @Param("statusId") Integer statusId,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice);


}

