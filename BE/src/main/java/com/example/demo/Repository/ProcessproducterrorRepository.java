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

    @Query("SELECT u FROM Processproducterror u WHERE u.job.jobId = :query")
    List<Processproducterror> getProcessproducterrorByJobId(int query);

    @Query("SELECT u FROM Processproducterror u" +
            " LEFT JOIN u.job j WHERE j.jobId = :query AND u.isFixed = false")
    List<Processproducterror> getProcessproducterrorByJobIdHaveFixNotDone(int query);

    @Query("SELECT u FROM Processproducterror u WHERE u.requestProducts.requestProductId = :query")
    List<Processproducterror> getProcessproducterrorByRequestProductId(int query);

    @Query("SELECT u FROM Processproducterror u WHERE u.processProductErrorId = :query")
    Processproducterror FindByIdProductErrorId(int query);

    //tổng số lượng sản phẩm lỗi
    @Query("SELECT SUM(p.quantity) FROM Processproducterror p")
    Long countTotalQuantityProductError();
    //tổng số lượng sản phẩm lỗi đã fix
    @Query("SELECT SUM(p.quantity) FROM Processproducterror p WHERE p.isFixed=true")
    Long countTotalQuantityProductErrorFixDone();

}
