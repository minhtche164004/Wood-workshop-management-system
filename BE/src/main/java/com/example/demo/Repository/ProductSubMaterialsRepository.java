package com.example.demo.Repository;

import com.example.demo.Entity.ProductSubMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ProductSubMaterialsRepository extends JpaRepository<ProductSubMaterials,Integer> {

}
