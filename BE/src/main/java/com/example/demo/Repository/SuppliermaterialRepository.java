package com.example.demo.Repository;

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



}
