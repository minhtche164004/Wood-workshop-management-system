package com.example.demo.Repository;

import com.example.demo.Entity.Materials;
import org.hibernate.annotations.processing.Find;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface MaterialRepository extends JpaRepository<Materials,Integer> {
    List<Materials> findAll();
    int countByMaterialName(String materialName);

}
