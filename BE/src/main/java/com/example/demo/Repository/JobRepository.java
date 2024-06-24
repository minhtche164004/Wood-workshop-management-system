package com.example.demo.Repository;

import com.example.demo.Entity.Jobs;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Jobs,Integer> {

//    @Query("SELECT j.user FROM Jobs j GROUP BY j.user HAVING COUNT(j) < 3")
//    List<User> findUsersWithLessThenThreeJobs();


    //bắt những thợ mộc có công <= 3 (thợ mộc có position_id là bằng 1) DISTINCT là những bản ghi ko trùng lặp
    @Query("SELECT u FROM User u " +
            "WHERE u.position.position_id = 1 AND u.role.roleId= 4 AND " +
            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
    List<User> findUsersWithPosition1AndLessThan3Jobs();

    //bắt những thợ đánh nhám có công <= 3 (thợ mộc có position_id là bằng 2)
    @Query("SELECT u FROM User u " +
            "WHERE u.position.position_id = 2 AND u.role.roleId= 4 AND " +
            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
    List<User> findUsersWithPosition2AndLessThan3Jobs();

    //bắt những thợ sơn có công <= 3 (thợ mộc có position_id là bằng 3)
    @Query("SELECT u FROM User u " +
            "WHERE u.position.position_id = 3 AND u.role.roleId= 4 AND " +
            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
    List<User> findUsersWithPosition3AndLessThan3Jobs();

    @Query(value = "SELECT p.* FROM jobs p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Jobs findJobsTop(@Param("prefix") String prefix);


}
