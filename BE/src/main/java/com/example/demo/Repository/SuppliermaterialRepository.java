package com.example.demo.Repository;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Entity.Suppliermaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@EnableJpaRepositories
public interface SuppliermaterialRepository extends JpaRepository<Suppliermaterial,Integer> {
    List<Suppliermaterial> findAll();
    int countSuppliermaterialBySupplierName(String supplierName);

   // int countBySuppliermaterialName(String SuppliermaterialName);

    @Query("SELECT u FROM Suppliermaterial u WHERE u.supplierMaterial = :query")
    Suppliermaterial findById(int query);

    @Query("SELECT u FROM Suppliermaterial u WHERE u.supplierName = :query")
    Suppliermaterial findByName(String query);


    @Query("SELECT u FROM Suppliermaterial u  WHERE u.supplierName LIKE CONCAT('%', :keyword, '%')")
    List<Suppliermaterial> SearchSupplierByName(@Param("keyword") String keyword);

}
