package com.example.demo.Repository;

import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
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

    @Query("SELECT e FROM Employeematerials e WHERE e.requestProductsSubmaterials.requestProductsSubmaterialsId = :query")
    List<Employeematerials> findEmployeematerialsByRequestProductId(int query);

    @Query("SELECT e FROM Employeematerials e WHERE e.productSubMaterial.productSubMaterialId = :query")
    List<Employeematerials> findEmployeematerialsByProductId(int query);


}
