package com.example.demo.Repository;

import com.example.demo.Entity.Requestimages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface RequestimagesRepository extends JpaRepository<Requestimages,Integer> {
}
