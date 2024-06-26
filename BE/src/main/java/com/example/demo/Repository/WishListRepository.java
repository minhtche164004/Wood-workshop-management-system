package com.example.demo.Repository;

import com.example.demo.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList,Integer> {

    @Query("SELECT u FROM WishList u WHERE u.user.userId = :query")
    List<WishList> findByUserID(int query);

    @Query("SELECT u FROM WishList u WHERE u.product.productId = :query")
    List<WishList> findByProductID(int query);

}
