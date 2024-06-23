package com.example.demo.Repository;

import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Jobs,Integer> {

//    @Query("SELECT j.user FROM Jobs j GROUP BY j.user HAVING COUNT(j) < 3")
//    List<User> findUsersWithLessThenThreeJobs();
}
