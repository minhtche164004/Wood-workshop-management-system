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

    @Query(value = "WITH EmployeeMaterialsWithRowNumber AS (" +
            " SELECT " +
            "e.emp_material_id, u.user_id, iu.fullname, pos.position_name,\n" +
            " ep.sub_material, sm.sub_material_name, e.total_material,\n" +
            " DATE(j.time_start) AS timeStart, j.code,\n" +
            " ROW_NUMBER() OVER (PARTITION BY e.emp_material_id ORDER BY j.time_start DESC) AS rn\n" +
            " FROM employee_materials e\n" +
            " LEFT JOIN users u ON e.employee_id = u.user_id\n" +
            " LEFT JOIN information_user iu ON iu.infor_id = u.infor_id\n" +
            " LEFT JOIN positions pos ON u.position_id = pos.position_id\n" +
            "LEFT JOIN product_sub_materials ep ON e.product_sub_material_id = ep.product_sub_material_id\n" +
            " LEFT JOIN input_sub_material s1 ON ep.input_id = s1.input_id\n" +
            " LEFT JOIN sub_materials sm ON sm.sub_material_id = s1.sub_material_id\n" +
            " JOIN jobs j ON u.user_id = j.user_id\n" +
            " WHERE request_products_submaterials_id is null  \n" +
            ")\n" +
            "SELECT * FROM EmployeeMaterialsWithRowNumber WHERE rn = 1",
            nativeQuery = true)
    List<Object[]> getAllEmployeeMate1();

    @Query(value = "WITH EmployeeMaterialsWithRowNumber AS (\n" +
            "    SELECT \n" +
            "        e.emp_material_id, u.user_id, iu.fullname, pos.position_name, \n" +
            "        ep.sub_material, sm.sub_material_name, e.total_material, \n" +
            "        DATE(j.time_start) AS timeStart, j.code,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY e.emp_material_id ORDER BY j.time_start DESC) AS rn\n" +
            "    FROM employee_materials e\n" +
            "    LEFT JOIN users u ON e.employee_id = u.user_id\n" +
            "    LEFT JOIN information_user iu ON iu.infor_id = u.infor_id\n" +
            "    LEFT JOIN positions pos ON u.position_id = pos.position_id\n" +
            "    LEFT JOIN request_products_submaterials ep ON e.request_products_submaterials_id = ep.request_products_submaterials_id\n" +
            "    LEFT JOIN input_sub_material s1 ON ep.input_id = s1.input_id\n" +
            "    LEFT JOIN sub_materials sm ON sm.sub_material_id = s1.sub_material_id\n" +
            "    JOIN jobs j ON u.user_id = j.user_id\n" +
            "    WHERE product_sub_material_id is null \n" +
            ")\n" +
            "SELECT *\n" +
            "FROM EmployeeMaterialsWithRowNumber\n" +
            "WHERE rn = 1",
            nativeQuery = true)
    List<Object[]> getAllEmployeeMate2();

    @Query(value = "WITH EmployeeMaterialsWithRowNumber AS (" +
            " SELECT " +
            "e.emp_material_id, u.user_id, iu.fullname, pos.position_name,\n" +
            " ep.sub_material, sm.sub_material_name, e.total_material,\n" +
            " DATE(j.time_start) AS timeStart, j.code,\n" +
            " ROW_NUMBER() OVER (PARTITION BY e.emp_material_id ORDER BY j.time_start DESC) AS rn\n" +
            " FROM employee_materials e\n" +
            " LEFT JOIN users u ON e.employee_id = u.user_id\n" +
            " LEFT JOIN information_user iu ON iu.infor_id = u.infor_id\n" +
            " LEFT JOIN positions pos ON u.position_id = pos.position_id\n" +
            "LEFT JOIN product_sub_materials ep ON e.product_sub_material_id = ep.product_sub_material_id\n" +
            " LEFT JOIN input_sub_material s1 ON ep.input_id = s1.input_id\n" +
            " LEFT JOIN sub_materials sm ON sm.sub_material_id = s1.sub_material_id\n" +
            " JOIN jobs j ON u.user_id = j.user_id\n" +
            " WHERE request_products_submaterials_id is null and iu.fullname LIKE CONCAT('%', :keyword, '%') \n" +
            ")\n" +
            "SELECT * FROM EmployeeMaterialsWithRowNumber WHERE rn = 1",
            nativeQuery = true)
    List<Object[]> getAllEmployeeMate1Search(@Param("keyword") String keyword);


    @Query(value = "WITH EmployeeMaterialsWithRowNumber AS (\n" +
            "    SELECT \n" +
            "        e.emp_material_id, u.user_id, iu.fullname, pos.position_name, \n" +
            "        ep.sub_material, sm.sub_material_name, e.total_material, \n" +
            "        DATE(j.time_start) AS timeStart, j.code,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY e.emp_material_id ORDER BY j.time_start DESC) AS rn\n" +
            "    FROM employee_materials e\n" +
            "    LEFT JOIN users u ON e.employee_id = u.user_id\n" +
            "    LEFT JOIN information_user iu ON iu.infor_id = u.infor_id\n" +
            "    LEFT JOIN positions pos ON u.position_id = pos.position_id\n" +
            "    LEFT JOIN request_products_submaterials ep ON e.request_products_submaterials_id = ep.request_products_submaterials_id\n" +
            "    LEFT JOIN input_sub_material s1 ON ep.input_id = s1.input_id\n" +
            "    LEFT JOIN sub_materials sm ON sm.sub_material_id = s1.sub_material_id\n" +
            "    JOIN jobs j ON u.user_id = j.user_id\n" +
            "    WHERE product_sub_material_id is null and iu.fullname LIKE CONCAT('%', :keyword, '%')\n" +
            ")\n" +
            "SELECT *\n" +
            "FROM EmployeeMaterialsWithRowNumber\n" +
            "WHERE rn = 1",
            nativeQuery = true)
    List<Object[]> getAllEmployeeMate2Search(@Param("keyword") String keyword);








    @Query("SELECT DISTINCT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, ep.subMaterial," +
            " s.subMaterialName, e.total_material, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.productSubMaterial ep " +
            "LEFT JOIN ep.inputSubMaterial.subMaterials s " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE ep IS NOT NULL AND u.userInfor.fullname LIKE CONCAT('%', :keyword, '%') " +
            "UNION  " +
            "SELECT new com.example.demo.Dto.JobDTO.Employee_MaterialDTO(" +
            "e.empMaterialId, u.userId, u.userInfor.fullname, pos.position_name, er.subMaterial, s.subMaterialName, e.total_material, CAST(j.timeStart AS date), j.code) " +
            "FROM Employeematerials e " +
            "LEFT JOIN e.employee u " +
            "LEFT JOIN u.position pos " +
            "LEFT JOIN e.requestProductsSubmaterials er " +
            "LEFT JOIN er.inputSubMaterial.subMaterials s " +
            "LEFT JOIN Jobs j ON u.userId = j.user.userId " +
            "WHERE er IS NOT NULL AND u.userInfor.fullname LIKE CONCAT('%', :keyword, '%')")
    List<Employee_MaterialDTO> getAllEmployeeMateByNameEmployee(@Param("keyword") String keyword);











}
