package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
import com.example.demo.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface Employee_Material_Repository extends JpaRepository<Employeematerials,Integer> {

    @Query("SELECT u FROM Employeematerials u  WHERE u.employee.userInfor.fullname LIKE CONCAT('%', :keyword, '%')")
    List<Employeematerials> findEmployeematerialsByName(@Param("keyword") String keyword);

    @Query("SELECT u.productSubMaterial.quantity FROM Employeematerials u WHERE u.empMaterialId = :empMaterialId")
    Double findByProductSubMaterialByEmployeeMaterialsId(int empMaterialId);

    @Query("SELECT u.requestProductsSubmaterials.quantity FROM Employeematerials u WHERE u.empMaterialId = :empMaterialId")
    Double findByRequestSubMaterialByEmployeeMaterialsId(int empMaterialId);


    @Query("SELECT e FROM Employeematerials e" +
            " LEFT JOIN e.productSubMaterial p" +
            " LEFT JOIN p.product pr" +
            " WHERE pr.productId = :query")
    List<Employeematerials> findEmployeematerialsByProductId(int query);

    @Query("SELECT e FROM Employeematerials e" +
            " LEFT JOIN e.requestProductsSubmaterials p" +
            " LEFT JOIN p.requestProduct pr" +
            " WHERE pr.requestProductId = :query")
    List<Employeematerials> findEmployeematerialsByRequestProductId(int query);

    @Query("SELECT e FROM Employeematerials e" +
            " LEFT JOIN e.employee ep" +
            " LEFT JOIN e.jobs j" +
            " WHERE j.jobId = :jobId AND ep.userId = :userId")
    List<Employeematerials> findEmployeematerialsByJobIdAndUserId(@Param("jobId") Integer jobId, @Param("userId") Integer userId);

    @Query("SELECT e FROM Employeematerials e" +
            " LEFT JOIN e.employee ep" +
            " LEFT JOIN e.jobs j" +
            " WHERE j.jobId = :jobId")
    List<Employeematerials> findEmployeematerialsByJobId(@Param("jobId") Integer jobId);

    @Query("SELECT e FROM Employeematerials e" +
            " LEFT JOIN e.jobs j" +
            " WHERE j.jobId = :query")
    List<Employeematerials> findEmployeematerialsByJobId(int query);


//    @Query("SELECT e FROM Employeematerials e" +
//            " LEFT JOIN e.productSubMaterial p" +
//            " LEFT JOIN e.employee u" +
//            " LEFT JOIN Jobs j ON u.userId = j.user.userId " +
//            " WHERE j.jobId = :jobId AND u.userId = :userId")
//    List<Employeematerials> findEmployeematerialsByJobId(int jobId,int userId);

//    @Query("SELECT e FROM Employeematerials e " +
//            "JOIN e.employee u " +
//            "JOIN u.jobs j " + // Sửa đổi tại đây
//            "WHERE j.jobId = :jobId AND u.userId = :userId")
//    List<Employeematerials> findEmployeematerialsByJobId(int jobId, int userId);




    @Query("SELECT DISTINCT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, ep.subMaterial, s.subMaterialName, ep.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.productSubMaterial ep " +
            "LEFT JOIN ep.inputSubMaterial.subMaterials s " +
            " JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE ep IS NOT NULL " +
            "UNION  " +
            "SELECT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, er.subMaterial, s.subMaterialName, er.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.requestProductsSubmaterials er " +
            "LEFT JOIN er.inputSubMaterial.subMaterials s " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE er IS NOT NULL")
    List<Employee_MaterialDTO> getAllEmployeeMate();


    @Query("SELECT DISTINCT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, ep.subMaterial, s.subMaterialName, ep.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.productSubMaterial ep " +
            "LEFT JOIN ep.inputSubMaterial.subMaterials s " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE ep IS NOT NULL AND u.userInfor.fullname LIKE CONCAT('%', :keyword, '%') " +
            "UNION  " +
            "SELECT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, er.subMaterial, s.subMaterialName, er.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.requestProductsSubmaterials er " +
            "LEFT JOIN er.inputSubMaterial.subMaterials s " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE er IS NOT NULL AND u.userInfor.fullname LIKE CONCAT('%', :keyword, '%')")
    List<Employee_MaterialDTO> getAllEmployeeMateByNameEmployee(@Param("keyword") String keyword);











}
