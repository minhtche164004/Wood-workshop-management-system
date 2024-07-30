package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.OrderDTO.OderDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Jobs,Integer> {

//    @Query("SELECT j.user FROM Jobs j GROUP BY j.user HAVING COUNT(j) < 3")
//    List<User> findUsersWithLessThenThreeJobs();


//    //bắt những thợ mộc có công <= 3 (thợ mộc có position_id là bằng 1) DISTINCT là những bản ghi ko trùng lặp
//    @Query("SELECT u FROM User u " +
//            "WHERE u.position.position_id = 1 AND u.role.roleId= 4 AND " +
//            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
//    List<User> findUsersWithPosition1AndLessThan3Jobs();
//
//    //bắt những thợ đánh nhám có công <= 3 (thợ mộc có position_id là bằng 2)
//    @Query("SELECT u FROM User u " +
//            "WHERE u.position.position_id = 2 AND u.role.roleId= 4 AND " +
//            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
//    List<User> findUsersWithPosition2AndLessThan3Jobs();

    //bắt những thợ sơn có công <= 3 (thợ mộc có position_id là bằng 3)
//    @Query("SELECT u FROM User u " +
//            "WHERE u.position.position_id = :type AND u.role.roleId= 4 AND " +
//            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId) < 3")
//    List<User> findUsersWithPositionAndLessThan3Jobs(@Param("type") int type);

    @Query("SELECT o.totalAmount FROM Orders o JOIN Orderdetails od ON o.orderId = od.order.orderId JOIN Jobs j ON od.orderDetailId = j.orderdetails.orderDetailId WHERE j.jobId = ?1")
    BigDecimal findToTalAmountOrderByJobId(int jobId);

    @Query("SELECT CASE WHEN j.product IS NOT NULL THEN true ELSE false END FROM Jobs j WHERE j.jobId = :jobId")
    boolean isProductJob(@Param("jobId") int jobId);


    @Transactional
    @Modifying
    @Query("UPDATE Orders o " +
            "SET o.discount = ?2 " +
            "WHERE o.orderId IN (" +
            "    SELECT od.order.orderId " +
            "    FROM Orderdetails od " +
            "    JOIN Jobs j ON od.orderDetailId = j.orderdetails.orderDetailId " +
            "    WHERE j.jobId = ?1" +
            ")")
    void updateOrderDiscountByJobId(int jobId, BigDecimal discount);


    @Query("SELECT u FROM User u " +
            "WHERE u.position.position_id = :type AND u.role.roleId= 4 AND " +
            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId AND j.job_log = false ) < 3")
    List<User> findUsersWithPositionAndLessThan3Jobs(@Param("type") int type);

    @Query(value = "SELECT COUNT(*) FROM jobs j WHERE j.user_id = :userId", nativeQuery = true)
    int countJobsByUserId(@Param("userId") int userId);

    @Query(value = "SELECT p.* FROM jobs p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Jobs findJobsTop(@Param("prefix") String prefix);

    @Transactional
    @Modifying
    @Query("DELETE FROM Jobs p WHERE p.jobId = :jobId")
    void deleteJobById(@Param("jobId") int jobId);


    @Query("SELECT u FROM Jobs u WHERE u.orderdetails.order.code = :query")
    List<Jobs> getJobByOrderDetailByOrderCode(String query);

    @Query("SELECT u FROM Jobs u WHERE u.jobId = :query")
    Jobs getJobById(int query);

    @Query("SELECT u FROM Jobs u WHERE u.product.productId = :query")
    List<Jobs> getJobByProductId(int query);

    @Query("SELECT u FROM Jobs u WHERE u.requestProducts.requestProductId = :query AND u.status.status_id != 14") //nếu job có trạng thái là khác chờ đtawj cọc thì ko đc xoá các job ấy nữa
    List<Jobs> getJobByRequestProductId(int query);

    @Query("SELECT u FROM Jobs u WHERE u.job_log IS TRUE")
    List<Jobs> getJobWasDone();

//    @Query("SELECT u FROM Jobs u WHERE (u.user.userInfor.fullname LIKE CONCAT('%', :keyword, '%')) AND u.job_log IS TRUE")
//    List<Jobs> filterJobWasDoneByEmployeeName(String keyword);




    @Transactional
    @Modifying
    @Query("update Jobs u set u.status.status_id = ?2" +
            " where u.jobId = ?1")
    void updateStatus(int jobId, int status_id);

    @Query("SELECT u FROM Status_Job u WHERE u.type = :query")
    List<Status_Job> findByStatusByType(int query);





    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(" +
            "j.jobId,j.code,o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " + // Sử dụng COALESCE
            "FROM Jobs j " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " +
            "LEFT JOIN od.requestProduct p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN j.status s " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN j.processProductErrors e " +
            "WHERE j.job_log = false AND p.requestProductId IS NOT NULL AND s.status_id != 14 " +
            "GROUP BY j.jobId, j.code, o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, u.userId, u.username, pos.position_id, pos.position_name")
    List<JobProductDTO> getRequestProductInJob();

    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(" +
            "j.jobId,j.code,o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " + // Sử dụng COALESCE
            "FROM Jobs j " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " +
            "LEFT JOIN od.requestProduct p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN j.status s " +
            "LEFT JOIN j.processProductErrors e " +
            "WHERE (p.requestProductName LIKE %:search% OR :search IS NULL) AND " +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(pos.position_id = :position_id OR :position_id IS NULL) AND j.job_log = false AND p.requestProductId IS NOT NULL AND s.status_id != 14 "+
            "GROUP BY j.jobId, j.code, o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, u.userId, u.username, pos.position_id, pos.position_name")

    List<JobProductDTO> MultiFilterRequestProductInJob(@Param("search") String search,
                                                       @Param("status_id") Integer status_id,
                                                       @Param("position_id") Integer position_id);


    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(j.jobId,j.code,null, COALESCE(p.productId, 0) ,COALESCE(p.productName, '') ,  COALESCE(p.description, ''),COALESCE(j.cost, 0) , j.status,COALESCE(j.quantityProduct, 0) ," +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " +
            "FROM Jobs j " +
            "LEFT JOIN j.product p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN j.status s " +
            "LEFT JOIN j.processProductErrors e " +
            "WHERE (p.productName LIKE %:search% OR :search IS NULL) AND " +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(pos.position_id = :position_id OR :position_id IS NULL) AND p.productId IS NOT NULL AND j.job_log = false"+
            " GROUP BY j.jobId, j.code, p.productId, p.productName, p.description, p.price, j.status, j.quantityProduct, u.userId, u.username, pos.position_id, pos.position_name")
    List<JobProductDTO> MultiFilterListProductJob(@Param("search") String search,
                                                  @Param("status_id") Integer status_id,
                                                  @Param("position_id") Integer position_id);






    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(j.jobId,j.code,null, COALESCE(p.productId, 0) ,COALESCE(p.productName, '') ,  COALESCE(p.description, ''),COALESCE(j.cost, 0) , j.status,COALESCE(j.quantityProduct, 0) ," +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " +
            "FROM Jobs j " +
            "LEFT JOIN j.product p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +  // Thêm JOIN với Position
            "LEFT JOIN j.processProductErrors e " +
            "WHERE p.productId IS NOT NULL AND j.job_log = false" +
            " GROUP BY j.jobId, j.code, p.productId, p.productName, p.description, p.price, j.status, j.quantityProduct, u.userId, u.username, pos.position_id, pos.position_name")
    List<JobProductDTO> getListProductJob();

    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(j.jobId,j.code,null, COALESCE(p.productId, 0),COALESCE(p.productName, '') ,COALESCE(p.description, '') ,COALESCE(j.cost, 0), j.status, COALESCE(j.quantityProduct, '') , " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " +  // Sử dụng COALESCE
            "FROM Jobs j " +
            "JOIN j.product p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN j.processProductErrors e " +
            "WHERE (p.productName LIKE CONCAT('%', :keyword, '%') OR u.username LIKE CONCAT('%', :keyword, '%')) AND j.job_log = false" +
            " GROUP BY j.jobId, j.code, p.productId, p.productName, p.description, p.price, j.status, j.quantityProduct, u.userId, u.username, pos.position_id, pos.position_name")
    List<JobProductDTO> getListProductJobByNameOrCodeProduct(@Param("keyword") String keyword);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(" +
            "j.jobId,j.code,o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, ''),COALESCE(MIN(e.processProductErrorId),0L),COALESCE(j.reassigned, false)) " + // Sử dụng COALESCE
            "FROM Jobs j " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " +
            "LEFT JOIN od.requestProduct p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN j.status s " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN j.processProductErrors e " +
            "WHERE p.requestProductId IS NOT NULL AND (j.orderdetails.order.code LIKE CONCAT('%', :keyword, '%') OR j.requestProducts.requestProductName LIKE CONCAT('%', :keyword, '%')) AND j.job_log = false AND s.status_id != 14"+
            "GROUP BY j.jobId, j.code, o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, u.userId, u.username, pos.position_id, pos.position_name")
    List<JobProductDTO> getRequestProductInOrderDetailByCode(@Param("keyword") String keyword);


    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, uf.fullname,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.userInfor uf" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE j.job_log = true AND j.status.status_id != 10")
    List<JobDoneDTO> findAllJobForEmployeeDone();

    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, uf.fullname,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.userInfor uf" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE  u.userId = :userId AND j.status.status_id != 10")
    //j.job_log = true AND
    List<JobDoneDTO> findAllJobForDoneByEmployeeID(int userId);

    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, uf.fullname,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.userInfor uf" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE ( uf.fullname LIKE %:search% OR :search IS NULL) AND" +
            "(s.status_id = :status_id OR :status_id IS NULL) AND " +
            "(p.position_id = :position_id OR :position_id IS NULL) AND j.job_log = true AND j.status.status_id != 10")
    List<JobDoneDTO> MultiFilterJobDone(@Param("search") String search,
                                               @Param("status_id") Integer status_id,
                                               @Param("position_id") Integer position_id);



    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, uf.fullname,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.userInfor uf" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE  u.userId = :userId AND j.code LIKE CONCAT('%', :query, '%') AND j.status.status_id != 10")
        //j.job_log = true AND
    List<JobDoneDTO> findAllJobForDoneByEmployeeIDWithJobCode(int userId,String query);

    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, ui.fullname, p.position_id, p.position_name, s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" +
            " FROM Jobs j" +
            " JOIN j.user u" +
//            " JOIN u.userInfor uf" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " JOIN u.userInfor ui" + // Thêm JOIN với UserInfor
            " WHERE ui.fullname LIKE :keyword AND j.job_log = true AND j.status.status_id != 10") // Sửa điều kiện và kiểu dữ liệu
    List<JobDoneDTO> filterJobWasDoneByEmployeeName(@Param("keyword") String keyword);



//    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
//            "p.processProductErrorId,COALESCE(j.code, 0), COALESCE(p.description, ''),COALESCE(p.isFixed, false),COALESCE(p.solution, ''),COALESCE(j.job_name, ''),COALESCE(j.jobId, 0), " +
//            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rq.requestProductId, 0), COALESCE(rq.requestProductName, ''),COALESCE(j.orderdetails.order.code, '')," +
//            "COALESCE(j.orderdetails.order.userInfor.fullname, ''),COALESCE(j.user.username, ''),COALESCE(ps.position_name, ''),COALESCE(ps.position_id, 0),COALESCE(p.quantity, 0))" + // Sử dụng COALESCE
//            " FROM Processproducterror p " +
//            " LEFT JOIN p.job j" +
//            " LEFT JOIN j.product pr"+
//            " LEFT JOIN j.user.position ps"+
//            " LEFT JOIN j.requestProducts rq")
//    List<ProductErrorAllDTO> getAllProductError();
@Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
        "p.processProductErrorId, " +
        "COALESCE(j.code, 0), " +
        "COALESCE(p.description, ''), " +
        "COALESCE(p.isFixed, false), " +
        "COALESCE(p.solution, ''), " +
        "COALESCE(j.job_name, ''), " +
        "COALESCE(j.jobId, 0), " +
        "COALESCE(pr.productId, 0), " +
        "COALESCE(pr.productName, ''), " +
        "COALESCE(rq.requestProductId, 0), " +
        "COALESCE(rq.requestProductName, ''), " +
        "COALESCE(o.code, ''), " +
        "COALESCE(ui.fullname, ''), " +
        "COALESCE(j.user.username, ''), " +
        "COALESCE(ps.position_name, ''), " +
        "COALESCE(ps.position_id, 0), " +
        "COALESCE(p.quantity, 0)) " +
        "FROM Processproducterror p " +
        "LEFT JOIN p.job j " +
        "LEFT JOIN j.product pr " +
        "LEFT JOIN j.user.position ps " +
        "LEFT JOIN j.requestProducts rq " +
        "LEFT JOIN j.orderdetails od " +
        "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
        "LEFT JOIN o.userInfor ui")
List<ProductErrorAllDTO> getAllProductError();

    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE j.jobId = :query")
    List<ProductErrorAllDTO> getAllProductErrorByJobId(int query);

    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE j.user.userId = :userId")
    List<ProductErrorAllDTO> getAllProductErrorForEmployee(int userId);


    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE p.processProductErrorId = :query")
    ProductErrorAllDTO getProductErrorDetailById(int query);



    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE  ( j.code LIKE %:search% OR :search IS NULL) AND " +
            "(p.isFixed = :isFixed OR :isFixed IS NULL)")
    List<ProductErrorAllDTO> MultiFilterErrorProductWithBoolean(@Param("search") String search,
                                               @Param("isFixed") Boolean isFixed);

    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE j.user.userId = :userId AND  ( j.code LIKE %:search% OR :search IS NULL) AND " +
            "(p.isFixed = :isFixed OR :isFixed IS NULL)")
    List<ProductErrorAllDTO> MultiFilterErrorProductWithBooleanForEmployee(int userId,@Param("search") String search,
                                                                @Param("isFixed") Boolean isFixed);


//    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
//            "p.processProductErrorId, " +
//            "COALESCE(j.code, 0), " +
//            "COALESCE(p.description, ''), " +
//            "COALESCE(p.isFixed, false), " +
//            "COALESCE(p.solution, ''), " +
//            "COALESCE(j.job_name, ''), " +
//            "COALESCE(j.jobId, 0), " +
//            "COALESCE(pr.productId, 0), " +
//            "COALESCE(pr.productName, ''), " +
//            "COALESCE(rq.requestProductId, 0), " +
//            "COALESCE(rq.requestProductName, ''), " +
//            "COALESCE(o.code, ''), " +
//            "COALESCE(ui.fullname, ''), " +
//            "COALESCE(j.user.username, ''), " +
//            "COALESCE(ps.position_name, ''), " +
//            "COALESCE(ps.position_id, 0), " +
//            "COALESCE(p.quantity, 0)) " +
//            "FROM Processproducterror p " +
//            "LEFT JOIN p.job j " +
//            "LEFT JOIN j.product pr " +
//            "LEFT JOIN j.user.position ps " +
//            "LEFT JOIN j.requestProducts rq " +
//            "LEFT JOIN j.orderdetails od " +
//            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
//            "LEFT JOIN o.userInfor ui WHERE j.user.userId = : userId")
//    List<ProductErrorAllDTO> getAllProductError(int userId);

    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE j.user.userId = :userId AND  ( j.code LIKE %:search% OR :search IS NULL)")
    List<ProductErrorAllDTO> MultiFilterErrorProductForEmployee(int userId,@Param("search") String search);


    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId, " +
            "COALESCE(j.code, 0), " +
            "COALESCE(p.description, ''), " +
            "COALESCE(p.isFixed, false), " +
            "COALESCE(p.solution, ''), " +
            "COALESCE(j.job_name, ''), " +
            "COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), " +
            "COALESCE(pr.productName, ''), " +
            "COALESCE(rq.requestProductId, 0), " +
            "COALESCE(rq.requestProductName, ''), " +
            "COALESCE(o.code, ''), " +
            "COALESCE(ui.fullname, ''), " +
            "COALESCE(j.user.username, ''), " +
            "COALESCE(ps.position_name, ''), " +
            "COALESCE(ps.position_id, 0), " +
            "COALESCE(p.quantity, 0)) " +
            "FROM Processproducterror p " +
            "LEFT JOIN p.job j " +
            "LEFT JOIN j.product pr " +
            "LEFT JOIN j.user.position ps " +
            "LEFT JOIN j.requestProducts rq " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " + // Sửa đổi đường dẫn đến orders
            "LEFT JOIN o.userInfor ui WHERE j.code LIKE %:search% OR :search IS NULL")
    List<ProductErrorAllDTO> MultiFilterErrorProduct(@Param("search") String search);







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



}
