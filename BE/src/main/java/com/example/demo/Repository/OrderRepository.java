package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.OderDTO;
import com.example.demo.Entity.Orders;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.Status_Order;
import jakarta.persistence.TemporalType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) " +
            ",COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " LEFT JOIN j.orderdetails od" +
            " LEFT JOIN od.order o"+
            " LEFT JOIN o.status s WHERE j.jobId = :query")
    OderDTO getOrderByJobId(int query);

    @Query("SELECT rp.completionTime " +
            "FROM RequestProducts rp " +
            "WHERE rp.orders.orderId = :orderId " +
            "ORDER BY rp.completionTime DESC " +
            "LIMIT 1") // Giới hạn chỉ lấy 1 bản ghi
    Date findLatestCompletionTimeByOrderId(@Param("orderId") int orderId);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) ,COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s" +
            " WHERE s.status_id = 3")
    List<OderDTO> getAllOrderWithStatus3();

    @Query("SELECT u.totalAmount FROM Orders u WHERE u.orderId = :query")
    BigDecimal findTotalAmountById(int query);

//    @Query("SELECT SUM(o.unitPrice*o.quantity) FROM Orderdetails o WHERE o.order.orderId = :orderId")
//    BigDecimal totalAmountBaseOrder(int orderId);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o SET o.status.status_id = 4 WHERE o.orderId = ?1")
    void updateOrderStatusFinish(int orderId);

    @Transactional
    @Modifying
    @Query("UPDATE Orders o " +
            "SET o.discount = ?2 ,o.totalAmount = ?3,o.status.status_id = 3,o.contractDate = ?4 " +
            "WHERE o.orderId = ?1")
    void updateOrderDiscountById(int orderId, BigDecimal discount,BigDecimal totalAmount,Date contractDate);

    @Query(value = "SELECT p.* FROM orders p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Orders findOrderTop(@Param("prefix") String prefix);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) " +
            ",COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s ORDER BY o.orderDate DESC")
    List<OderDTO> getAllOrder();

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) " +
            ",COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s WHERE o.specialOrder = TRUE")
    List<OderDTO> getAllOrderSpecial();

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount, COALESCE(s.status_id, 0) " +
            ", COALESCE(s.status_name, ''), COALESCE(o.paymentMethod, ''), COALESCE(o.deposite, 0) , COALESCE(o.specialOrder, false), o.contractDate)" +
            " FROM Orders o" +
            " LEFT JOIN o.status s" +
            " WHERE ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            //     " (:specialOrder IS NULL OR o.specialOrder = :specialOrder) AND " + // Sửa đổi tại đây
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL) "+
           " ORDER BY o.orderDate DESC")
    List<OderDTO> MultiFilterOrder(@Param("search") String search,
                                   @Param("status_id") Integer status_id,
                                   @Param("paymentMethod") Integer paymentMethod,
                                   //     @Param("specialOrder") Boolean specialOrder,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate);
    @Query("SELECT o" +
            " FROM Orders o" +
            " LEFT JOIN o.userInfor.user u" +
            " LEFT JOIN o.status s" +
            " WHERE u.userId = :userId AND ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            //     " (:specialOrder IS NULL OR o.specialOrder = :specialOrder) AND " + // Sửa đổi tại đây
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL)")
    List<Orders> MultiFilterOrderForEmployee(int userId,
            @Param("search") String search,
                                   @Param("status_id") Integer status_id,
                                   @Param("paymentMethod") Integer paymentMethod,
                                   //     @Param("specialOrder") Boolean specialOrder,
                                   @Param("startDate") Date startDate,
                                   @Param("endDate") Date endDate);

    @Query("SELECT o" +
            " FROM Orders o" +
            " LEFT JOIN o.userInfor.user u" +
            " LEFT JOIN o.status s" +
            " WHERE  u.userId = :userId AND ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            " (o.specialOrder = :specialOrder OR :specialOrder IS NULL) AND " + // Sửa đổi tại đây
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL)")
    List<Orders> MultiFilterOrderSpecialOrderForEmployee(int userId,@Param("search") String search,
                                               @Param("status_id") Integer status_id,
                                               @Param("paymentMethod") Integer paymentMethod,
                                               @Param("specialOrder") Boolean specialOrder,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);

    @Query("SELECT o" +
            " FROM Orders o" +
            " LEFT JOIN o.userInfor.user u" +
            " LEFT JOIN o.status s" +
            " WHERE u.userId = :userId AND ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL)")
    List<Orders> MultiFilterOrderWithoutOrderTypeForEmployee(int userId,@Param("search") String search,
                                                   @Param("status_id") Integer status_id,
                                                   @Param("paymentMethod") Integer paymentMethod,
                                                   @Param("startDate") Date startDate,
                                                   @Param("endDate") Date endDate);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount, COALESCE(s.status_id, 0) , COALESCE(s.status_name, ''), COALESCE(o.paymentMethod, ''), COALESCE(o.deposite, 0) , COALESCE(o.specialOrder, false), o.contractDate)" +
            " FROM Orders o" +
            " LEFT JOIN o.status s" +
            " WHERE ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            " (o.specialOrder = :specialOrder OR :specialOrder IS NULL) AND " + // Sửa đổi tại đây
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL) ORDER BY o.orderDate DESC")
    List<OderDTO> MultiFilterOrderSpecialOrder(@Param("search") String search,
                                               @Param("status_id") Integer status_id,
                                               @Param("paymentMethod") Integer paymentMethod,
                                               @Param("specialOrder") Boolean specialOrder,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount, COALESCE(s.status_id, 0) , COALESCE(s.status_name, ''), COALESCE(o.paymentMethod, ''), COALESCE(o.deposite, 0) , COALESCE(o.specialOrder, false), o.contractDate)" +
            " FROM Orders o" +
            " LEFT JOIN o.status s" +
            " WHERE ( o.code LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(o.paymentMethod = :paymentMethod OR :paymentMethod IS NULL) AND " +
            "(o.orderDate >= :startDate OR :startDate IS NULL) AND " +
            "(o.orderDate <= :endDate OR :endDate IS NULL) ORDER BY o.orderDate DESC")
    List<OderDTO> MultiFilterOrderWithoutOrderType(@Param("search") String search,
                                               @Param("status_id") Integer status_id,
                                               @Param("paymentMethod") Integer paymentMethod,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate);


    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount, COALESCE(s.status_id, 0) , COALESCE(s.status_name, ''), COALESCE(o.paymentMethod, ''), COALESCE(o.deposite, 0) , COALESCE(o.specialOrder, false), o.contractDate)" +
            " FROM Orders o" +
            " LEFT JOIN o.status s" +
            " WHERE o.specialOrder = :specialOrder OR :specialOrder IS NULL")
    List<OderDTO> findBySpecialOrder(@Param("specialOrder") boolean specialOrder);



    @Query("SELECT u FROM Orders u WHERE u.code = :query")
    Orders findByCode(String query);

    @Query("SELECT u FROM Orders u WHERE u.orderId = :query")
    Orders findById(int query);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) ,COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s WHERE o.address LIKE CONCAT('%', :keyword, '%') OR " +
            "o.code LIKE CONCAT('%', :keyword, '%')")
    List<OderDTO> findOrderByAddressorCode(@Param("keyword") String keyword);

//    @Query("SELECT u FROM Orders u  WHERE u.address LIKE CONCAT('%', :keyword, '%') OR " +
//            "u.code LIKE CONCAT('%', :keyword, '%')")
//    List<Orders> findOrderByAddressorCode(@Param("keyword") String keyword);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) ,COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false), o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<OderDTO> findByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

//    @Query("SELECT o FROM Orders o WHERE o.orderDate BETWEEN :startDate AND :endDate")
//    List<Orders> findByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.OderDTO(" +
            "COALESCE(o.code, ''), o.orderId, COALESCE(o.orderDate, '') , o.totalAmount,COALESCE(s.status_id, 0) ,COALESCE(s.status_name, '') , COALESCE(o.paymentMethod, ''),COALESCE(o.deposite, 0) ,COALESCE(o.specialOrder, false),o.contractDate)" + // Sử dụng COALESCE
            " FROM Orders o" +
            " LEFT JOIN o.status s WHERE o.status.status_id = :query")
    List<OderDTO> filterByStatus(int query);

//    @Query("SELECT u FROM Orders u WHERE u.status.status_id = :query")
//    List<Orders> filterByStatus(int query);

    @Query("SELECT u FROM Orders u WHERE u.userInfor.user.userId = :query")
    List<Orders> findHistoryOrder(int query);


    @Transactional
    @Modifying
    @Query("update Orders u set u.status.status_id = ?2 where u.orderId = ?1")
    void UpdateStatusOrder(int orderId, int status_id);

        @Query("SELECT u FROM Orders u WHERE u.userInfor.user.userId = :query ")
    List<Orders> findByUserId(int query);

        @Transactional
    @Modifying
    @Query("UPDATE Orders u SET u.status.status_id = :status_id WHERE u.userInfor.user.userId = :requestId")
    void updateStatus(@Param("requestId") int requestId, @Param("status_id") int status_id);


        @Transactional
    @Modifying
    @Query("update Orders u set u.description = ?2 where u.orderId = ?1")
    void updateRequest(int requestId,String description);



//    @Query(value = "SELECT p.* FROM requests p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
//    Requests findRequestTop(@Param("prefix") String prefix);

//    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
//    Requests findById(int query);


//    @Query("SELECT u FROM Requests u WHERE u.status.status_id = 1")
//    List<Requests> findAllRequest();

//    @Query("SELECT u FROM Requests u WHERE u.status.status_id = 2")
//    List<Requests> findAllRequestAccept();

//    @Query("SELECT u FROM Requests u WHERE u.requestId = :query")
//    Requests findByRequestProductId(int query);



//    @Query("SELECT u FROM Requests u WHERE u.user.userId = :query AND u.status.status_id = 2")
//    List<Requests> findByUserId(int query);












}
