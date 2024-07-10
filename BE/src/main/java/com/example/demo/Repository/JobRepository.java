package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.JobDoneDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.ProductDTO.ProductErrorAllDTO;
import com.example.demo.Entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT u FROM User u " +
            "WHERE u.position.position_id = :type AND u.role.roleId= 4 AND " +
            "(SELECT COUNT( j.jobId) FROM Jobs j WHERE j.user.userId = u.userId AND j.job_log = true ) < 3")
    List<User> findUsersWithPositionAndLessThan3Jobs(@Param("type") int type);

    @Query(value = "SELECT COUNT(*) FROM jobs j WHERE j.user_id = :userId", nativeQuery = true)
    int countJobsByUserId(@Param("userId") int userId);

    @Query(value = "SELECT p.* FROM jobs p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    Jobs findJobsTop(@Param("prefix") String prefix);


    @Query("SELECT u FROM Jobs u WHERE u.orderdetails.order.code = :query")
    List<Jobs> getJobByOrderDetailByOrderCode(String query);

    @Query("SELECT u FROM Jobs u WHERE u.jobId = :query")
    Jobs getJobById(int query);

    @Query("SELECT u FROM Jobs u WHERE u.product.productId = :query")
    List<Jobs> getJobByProductId(int query);

    @Query("SELECT u FROM Jobs u WHERE u.requestProducts.requestProductId = :query")
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
            "j.jobId,o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, '')) " + // Sử dụng COALESCE
            "FROM Jobs j " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " +
            "LEFT JOIN od.requestProduct p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "WHERE p.requestProductId IS NOT NULL AND j.orderdetails.order.code = :query AND j.job_log = false")
    List<JobProductDTO> getRequestProductInOrderDetailByCode(@Param("query") String query);

    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(" +
            "j.jobId,o.code, p.requestProductId, p.requestProductName, p.description, p.price, j.status, od.quantity, " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, '')) " + // Sử dụng COALESCE
            "FROM Jobs j " +
            "LEFT JOIN j.orderdetails od " +
            "LEFT JOIN od.order o " +
            "LEFT JOIN od.requestProduct p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "WHERE j.job_log = false AND p.requestProductId IS NOT NULL")
    List<JobProductDTO> getRequestProductInJob();






    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(j.jobId,null, COALESCE(p.productId, 0) ,COALESCE(p.productName, '') ,  COALESCE(p.description, ''),COALESCE(j.cost, 0) , j.status,COALESCE(j.quantityProduct, 0) ," +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, '')) " +
            "FROM Jobs j " +
            "LEFT JOIN j.product p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +  // Thêm JOIN với Position
            "WHERE p.productId IS NOT NULL AND j.job_log = false")
    List<JobProductDTO> getListProductJob();

    @Query("SELECT new com.example.demo.Dto.OrderDTO.JobProductDTO(j.jobId,null, COALESCE(p.productId, 0),COALESCE(p.productName, '') ,COALESCE(p.description, '') ,COALESCE(j.cost, 0), j.status, COALESCE(j.quantityProduct, '') , " +
            "COALESCE(u.userId, 0), COALESCE(u.username, ''), COALESCE(pos.position_id, 0), COALESCE(pos.position_name, '')) " +  // Sử dụng COALESCE
            "FROM Jobs j " +
            "JOIN j.product p " +
            "LEFT JOIN j.user u " +
            "LEFT JOIN u.position pos " +
            "WHERE (p.productName LIKE CONCAT('%', :keyword, '%') OR u.username LIKE CONCAT('%', :keyword, '%')) AND j.job_log = false")
    List<JobProductDTO> getListProductJobByNameOrCodeProduct(@Param("keyword") String keyword);


    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, u.username,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE j.job_log = true")
    List<JobDoneDTO> findAllJobForEmployeeDone();

    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, u.username,COALESCE(p.position_id, 0) ,COALESCE(p.position_name, '') , s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" + // Sử dụng COALESCE
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " WHERE j.job_log = true AND u.userId = :userId")
    List<JobDoneDTO> findAllJobForDoneByEmployeeID(int userId);

    @Query("SELECT new com.example.demo.Dto.JobDTO.JobDoneDTO(" +
            "j.jobId, j.job_name, u.userId, u.username, p.position_id, p.position_name, s.status_id, s.status_name, j.cost, " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rp.requestProductId, 0), COALESCE(rp.requestProductName, ''), j.quantityProduct,j.code)" +
            " FROM Jobs j" +
            " JOIN j.user u" +
            " JOIN u.position p" +
            " JOIN j.status s" +
            " LEFT JOIN j.product pr" +
            " LEFT JOIN j.requestProducts rp" +
            " JOIN u.userInfor ui" + // Thêm JOIN với UserInfor
            " WHERE ui.fullname LIKE :keyword AND j.job_log = true") // Sửa điều kiện và kiểu dữ liệu
    List<JobDoneDTO> filterJobWasDoneByEmployeeName(@Param("keyword") String keyword);



    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId,COALESCE(j.code, 0), COALESCE(p.description, ''),COALESCE(p.isFixed, false),COALESCE(p.solution, ''),COALESCE(j.job_name, ''),COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rq.requestProductId, 0), COALESCE(rq.requestProductName, ''),j.orderdetails.order.code," +
            "COALESCE(j.orderdetails.order.userInfor.fullname, ''),COALESCE(j.user.username, ''))" + // Sử dụng COALESCE
            " FROM Processproducterror p " +
            " LEFT JOIN p.job j" +
            " LEFT JOIN j.product pr"+
            " LEFT JOIN j.requestProducts rq")
    List<ProductErrorAllDTO> getAllProductError();
//    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
//            "p.processProductErrorId, CAST(COALESCE(j.code, '0') AS string), COALESCE(p.description, ''), CAST(p.isFixed AS boolean), COALESCE(p.solution, ''), COALESCE(j.job_name, ''), COALESCE(j.jobId, 0), " +
//            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rq.requestProductId, 0), COALESCE(rq.requestProductName, ''), COALESCE(j.orderdetails.order.code, '')," +
//            "COALESCE(j.orderdetails.order.userInfor.fullname, ''), COALESCE(j.user.username, ''))" + // Sử dụng COALESCE and casting
//            " FROM Processproducterror p " +
//            " LEFT JOIN p.job j" +
//            " LEFT JOIN j.product pr"+
//            " LEFT JOIN j.requestProducts rq")
//    List<ProductErrorAllDTO> getAllProductError();


    @Query("SELECT new com.example.demo.Dto.ProductDTO.ProductErrorAllDTO(" +
            "p.processProductErrorId,COALESCE(j.code, 0), COALESCE(p.description, ''),COALESCE(p.isFixed, false),COALESCE(p.solution, ''),COALESCE(j.job_name, ''),COALESCE(j.jobId, 0), " +
            "COALESCE(pr.productId, 0), COALESCE(pr.productName, ''), COALESCE(rq.requestProductId, 0), COALESCE(rq.requestProductName, ''),j.orderdetails.order.code," +
            "COALESCE(j.orderdetails.order.userInfor.fullname, ''),COALESCE(j.user.username, ''))" + // Sử dụng COALESCE
            " FROM Processproducterror p " +
            " LEFT JOIN p.job j" +
            " LEFT JOIN j.product pr"+
            " LEFT JOIN j.requestProducts rq"+
            " LEFT JOIN j.orderdetails.order o WHERE p.processProductErrorId = :query")
    ProductErrorAllDTO getProductErrorDetailById(int query);




}
