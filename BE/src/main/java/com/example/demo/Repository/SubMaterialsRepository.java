package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Entity.*;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface SubMaterialsRepository extends JpaRepository<SubMaterials,Integer> {

    @Query("SELECT u FROM SubMaterials u WHERE u.subMaterialId = :query")
    SubMaterials findById1(int query);

    @Query("SELECT u FROM Action_Type u  WHERE u.action_type_id = :query")
    Action_Type findByIdAction(int query);

    @Query("SELECT u FROM InputSubMaterial u")
    List<InputSubMaterial> getAllInputSubMaterial();

    @Query("SELECT i FROM InputSubMaterial i" +
            " LEFT JOIN i.subMaterials s  " +
            " LEFT JOIN i.actionType a  WHERE " +
            "(s.subMaterialName LIKE %:search% OR :search IS NULL) AND " +
            "(s.material.materialId = :materialId OR :materialId IS NULL) AND " +
            "(a.action_type_id = :action_type_id OR :action_type_id IS NULL) AND " +
            "(i.date_input >= :startDate OR :startDate IS NULL) AND " +
            "(i.date_input <= :endDate OR :endDate IS NULL) AND " +
            "(i.unit_price >= :minPrice OR :minPrice IS NULL) AND " +
            "(i.unit_price <= :maxPrice OR :maxPrice IS NULL)")
    List<InputSubMaterial> MultiFilterInputSubMaterial(@Param("search") String search,
                                               @Param("materialId") Integer materialId, @Param("action_type_id") Integer action_type_id,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate,
                                               @Param("minPrice") BigDecimal minPrice,
                                               @Param("maxPrice") BigDecimal maxPrice);


    @Query("SELECT u FROM SubMaterials u WHERE u.subMaterialName = :query AND u.material.materialName = :materialName")
    SubMaterials findBySubmaterialNameAndMaterialName(@Param("query") String subMaterialName, @Param("materialName") String materialName);

    @Query("SELECT u FROM SubMaterials u WHERE u.subMaterialName = :query AND u.material.materialName = :materialName AND u.unitPrice = :unitPrice")
    SubMaterials findBySubmaterialNameAndMaterialNameAndPrice(@Param("query") String subMaterialName, @Param("materialName") String materialName,@Param("unitPrice") BigDecimal unitPrice);


    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), s.quantity, s.unitPrice,s.inputPrice,m.type) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM SubMaterials s " +
            "LEFT JOIN s.material m")
    List<SubMaterialViewDTO> getAllSubmaterial();

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), s.quantity, s.unitPrice,s.inputPrice,m.type) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM SubMaterials s " +
            "LEFT JOIN s.material m WHERE s.material.materialId = :query")
    List<SubMaterialViewDTO> findSubMaterialIdByMaterial(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), s.quantity, s.unitPrice,s.inputPrice,m.type) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM SubMaterials s " +
            "LEFT JOIN s.material m WHERE s.subMaterialName LIKE CONCAT('%', :keyword, '%') OR " +
            "s.code LIKE CONCAT('%', :keyword, '%')")
    List<SubMaterialViewDTO> findSubMaterialsByNameCode(@Param("keyword") String keyword);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), s.quantity, s.unitPrice,s.inputPrice,m.type) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM SubMaterials s " +
            "LEFT JOIN s.material m WHERE s.subMaterialId = :subMaterialId")
    SubMaterialViewDTO findSubMaterialsById(int subMaterialId);


//    @Query("SELECT u FROM SubMaterials u WHERE u.material.materialId = :query")
//    List<SubMaterials> findSubMaterialIdByMaterial(int query);

    List<SubMaterials> findAll();
    int countBySubMaterialName(String SubMaterialName);

    @Query(value = "SELECT p.* FROM sub_materials p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    SubMaterials findSubMaterialsTop(@Param("prefix") String prefix);


    @Query("SELECT u.quantity FROM SubMaterials u WHERE u.subMaterialId = :query")
    Integer findQuantityBySubMaterialId(int query);




//    @Query("SELECT u FROM SubMaterials u  WHERE u.subMaterialName LIKE CONCAT('%', :keyword, '%') OR " +
//            "u.code LIKE CONCAT('%', :keyword, '%')")
//    List<SubMaterials> findSubMaterialsByNameCode(@Param("keyword") String keyword);


    @Transactional
    @Modifying
    @Query("update SubMaterials u set u.subMaterialName = ?2,u.description=?3,u.quantity=?4,u.unitPrice=?5" +
            " where u.subMaterialId = ?1")
    void updateSubMaterials(int productId, String subMaterialName, String description, Double quantity, BigDecimal unitPrice);








}
