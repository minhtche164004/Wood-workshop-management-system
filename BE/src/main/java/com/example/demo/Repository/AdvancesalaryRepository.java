package com.example.demo.Repository;

import com.example.demo.Entity.Advancesalary;
import com.example.demo.Entity.Products;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
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


    //hien tai lay fulllname thay cho username do khong tao DTO de chua fullname
    @Query("SELECT a FROM Advancesalary a LEFT JOIN a.user.userInfor i WHERE " +
            "(:fromDate IS NULL OR a.date >= :fromDate) AND " +
            "(:toDate IS NULL OR a.date <= :toDate) AND " +
            "(a.user.position.position_id IN :position_id OR :position_id IS NULL) AND " +
            "(:fullname IS NULL OR i.fullname LIKE %:fullname%) " +
            "ORDER BY a.date DESC")
    List<Advancesalary> filterAdvancesalary(@Param("fromDate") Date fromDate,
                                            @Param("toDate") Date toDate,
                                            @Param("position_id") Integer position_id,
                                            @Param("fullname") String fullname);



    //tính tổng lương nhân viên phải trả theo tháng và năm
        @Query("SELECT SUM(a.amount) FROM Advancesalary a WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year")
        BigDecimal findTotalSalaryByMonthAndYear(@Param("month") int month, @Param("year") int year);



}
