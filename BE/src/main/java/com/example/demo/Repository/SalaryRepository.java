package com.example.demo.Repository;

import com.example.demo.Entity.Salaries;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository  extends JpaRepository<Salaries, Integer> {
}
