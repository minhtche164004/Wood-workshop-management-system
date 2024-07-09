package com.example.demo.Repository;

import com.example.demo.Entity.Advancesalary;
import com.example.demo.Entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface AdvancesalaryRepository extends JpaRepository<Advancesalary,Integer> {

    @Query(value = "SELECT p.* FROM advancesalary p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Advancesalary findAdvancesalaryTop(@Param("prefix") String prefix);

    @Query("SELECT u FROM Advancesalary u WHERE u.user.userId = :query")
    List<Advancesalary> findByUserId(int query);

    @Query("SELECT u FROM Advancesalary u WHERE u.advanceSalaryId = :query")
    Advancesalary findById(int query);

    @Transactional
    @Modifying
    @Query("update Advancesalary u set u.isAdvanceSuccess = ?2 where u.advanceSalaryId = ?1")
    void update_banking(int advanceSalaryId, boolean isAdvanceSuccess);

    @Query("SELECT a FROM Advancesalary a WHERE " +
            "(:fromDate IS NULL OR a.date >= :fromDate) AND " +
            "(:toDate IS NULL OR a.date <= :toDate) AND " +
            "(a.user.position.position_id IN :position_id OR :position_id IS NULL) AND " +
            "(:employeeName IS NULL OR a.user.username LIKE %:employeeName%) " +
            "ORDER BY a.date DESC") // Sắp xếp theo ngày giảm dần (từ mới đến cũ)
    List<Advancesalary> filterAdvancesalary(@Param("fromDate") Date fromDate,
                                            @Param("toDate") Date toDate,
                                            @Param("position_id") Integer position_id,
                                            @Param("employeeName") String employeeName);

}
