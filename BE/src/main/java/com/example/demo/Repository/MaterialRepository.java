package com.example.demo.Repository;

import com.example.demo.Entity.Categories;
import com.example.demo.Entity.Materials;
import org.hibernate.annotations.processing.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface MaterialRepository extends JpaRepository<Materials,Integer> {
    List<Materials> findAll();

    int countByMaterialName(String materialName);

    @Query("SELECT u FROM Materials u WHERE u.materialId = :query")
    Materials findById1(int query);

}
