package com.example.demo.Repository;

import com.example.demo.Entity.Orderdetails;
import com.example.demo.Entity.Processproducterror;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessproducterrorRepository extends JpaRepository<Processproducterror,Integer> {

    @Query("SELECT u FROM Processproducterror u WHERE u.product.productId = :query")
    List<Processproducterror> getProcessproducterrorByProductId(int query);

    @Query("SELECT u FROM Processproducterror u WHERE u.requestProducts.requestProductId = :query")
    List<Processproducterror> getProcessproducterrorByRequestProductId(int query);

    @Query("SELECT u FROM Processproducterror u WHERE u.processProductErrorId = :query")
    Processproducterror FindByIdProductErrorId(int query);

    //tổng số lượng sản phẩm lỗi
    @Query("SELECT SUM(p.quantity) FROM Processproducterror p")
    Long countTotalQuantityProductError();

    @Query("SELECT SUM(p.quantity) FROM Processproducterror p WHERE P.isFixed==true")
    Long countTotalQuantityProductErrorFixDone();

}
