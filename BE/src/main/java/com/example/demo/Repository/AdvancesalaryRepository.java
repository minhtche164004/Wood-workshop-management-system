package com.example.demo.Repository;

import com.example.demo.Entity.Advancesalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvancesalaryRepository extends JpaRepository<Advancesalary,Integer> {
}
