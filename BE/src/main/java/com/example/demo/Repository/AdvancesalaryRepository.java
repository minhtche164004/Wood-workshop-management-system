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



    //------------------------thống kê ----------------------------------------//

    //    //đếm số lượng order theo tháng và năm
//    @Query("SELECT COUNT(*) FROM Orders o " +
//            "JOIN o.status s " +
//            "WHERE s.status_name = :status_name " +
//            "AND MONTH(o.timeFinish) = :month " +
//            "AND YEAR(j.timeFinish) = :year")
//    Long countCompletedOrderByMonthAndYear(
//            @Param("status_name") String status_name,
//            @Param("month") int month,
//            @Param("year") int year);

    //tính tổng lương nhân viên phải trả theo tháng và năm
    @Query("SELECT SUM(a.amount) FROM Advancesalary a WHERE MONTH(a.date) = :month AND YEAR(a.date) = :year")
    BigDecimal findTotalSalaryByMonthAndYear(@Param("month") int month, @Param("year") int year);

    //đếm số lượng job theo tháng và năm
    @Query("SELECT COUNT(*) FROM Jobs j " +
            "JOIN j.status s " +
            "WHERE s.status_name = :status_name " +
            "AND MONTH(j.timeFinish) = :month " +
            "AND YEAR(j.timeFinish) = :year")
    Long countCompletedJobsByMonthAndYear(
            @Param("status_name") String status_name,
            @Param("month") int month,
            @Param("year") int year);



    //số lượng sản phẩm có sẵn
    @Query("SELECT COUNT(*) FROM Products p")
    Long countProduct();

    //số lượng đơn hàng theo yêu cầu
    @Query("SELECT COUNT(*) FROM Orders o WHERE o.specialOrder = TRUE")
    Long countSpecialOrder();

    //tổng số lượng đơn hàng
    @Query("SELECT COUNT(*) FROM Orders o")
    Long countTotalOrder();

    //số lượng đơn hàng theo status(đã hoàn thành, đã đặt cọc, chưa đặt cọc, ...)
    @Query("SELECT COUNT(*) FROM Orders o WHERE o.status.status_id = :query")
    Long countOrderHaveDone(int query);

    // đếm số lượng nhân viên theo vị trí (thợ mộc , thợ sơn , thợ nhám)
    @Query("SELECT COUNT(*) FROM User u WHERE u.position.position_id = :query")
    Long countEmployeeWithTypePosition(int query);

    //tổng tiền các đơn hàng đã hoàn thành(status_id là 5, tức là đã hoàn thành)
    @Query("SELECT SUM(o.totalAmount) FROM Orders o WHERE o.status.status_id = 5")
    BigDecimal totalAmountOrderHaveDone();

    //tổng số tiền nhập nguyên vật liệu
    @Query("SELECT SUM(s.quantity*s.unitPrice) FROM SubMaterials s")
    BigDecimal totalAmountSubMaterial();













}
