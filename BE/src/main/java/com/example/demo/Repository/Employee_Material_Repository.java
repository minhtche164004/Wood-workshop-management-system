package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
import com.example.demo.Entity.Employeematerials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.Status_Job;
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

    @Query("SELECT DISTINCT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.username, pos.position_name, ep.subMaterial.subMaterialId, ep.subMaterial.subMaterialName, ep.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.productSubMaterial ep " +
            " JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE ep IS NOT NULL " +
            "UNION  " +
            "SELECT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.username, pos.position_name, er.subMaterial.subMaterialId, er.subMaterial.subMaterialName, er.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.requestProductsSubmaterials er " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE er IS NOT NULL")
    List<Employee_MaterialDTO> getAllEmployeeMate();


    @Query("SELECT DISTINCT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.username, pos.position_name, ep.subMaterial.subMaterialId, ep.subMaterial.subMaterialName, ep.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.productSubMaterial ep " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE ep IS NOT NULL AND u.username LIKE CONCAT('%', :keyword, '%') " +
            "UNION  " +
            "SELECT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.username, pos.position_name, er.subMaterial.subMaterialId, er.subMaterial.subMaterialName, er.quantity, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.requestProductsSubmaterials er " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE er IS NOT NULL AND u.username LIKE CONCAT('%', :keyword, '%')")
    List<Employee_MaterialDTO> getAllEmployeeMateByNameEmployee(@Param("keyword") String keyword);











}
