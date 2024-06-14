package com.example.demo.Repository;

import com.example.demo.Entity.Orderdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<Orderdetails,Integer> {
}
