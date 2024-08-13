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

    @Query("SELECT u FROM InputSubMaterial u ")
    List<InputSubMaterial> getAllInputSubMaterial();

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), latestInput.total_quantity, latestInput.out_price,latestInput.input_price,m.type,s.code,latestInput.input_id) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM SubMaterials s " +
            "LEFT JOIN (" +

            "SELECT ism.subMaterials.subMaterialId as subMaterialId, ism.quantity as total_quantity, ism.out_price as out_price, ism.input_price as input_price,ism.input_id as input_id" +
          " FROM InputSubMaterial ism " +
            "ORDER BY ism.date_input DESC " +
            "LIMIT 1 " +
            ") latestInput ON s.subMaterialId = latestInput.subMaterialId " +
            "LEFT JOIN s.material m WHERE (s.subMaterialName LIKE %:search% OR :search IS NULL) AND " +
            "(s.material.materialId = :materialId OR :materialId IS NULL)")
    List<SubMaterialViewDTO> MultiFilterSubmaterial(@Param("search") String search,
                                                    @Param("materialId") Integer materialId);

    @Query("SELECT i FROM InputSubMaterial i" +
            " LEFT JOIN i.subMaterials s  " +
            " LEFT JOIN i.actionType a  WHERE " +
            "(s.subMaterialName LIKE %:search% OR :search IS NULL) AND " +
            "(s.material.materialId = :materialId OR :materialId IS NULL) AND " +
            "(a.action_type_id = :action_type_id OR :action_type_id IS NULL) AND " +
            "(i.date_input >= :startDate OR :startDate IS NULL) AND " +
            "(i.date_input <= :endDate OR :endDate IS NULL) AND " +
            "(i.out_price >= :minPrice OR :minPrice IS NULL) AND " +
            "(i.out_price <= :maxPrice OR :maxPrice IS NULL)")
    List<InputSubMaterial> MultiFilterInputSubMaterial(@Param("search") String search,
                                               @Param("materialId") Integer materialId, @Param("action_type_id") Integer action_type_id,
                                               @Param("startDate") Date startDate,
                                               @Param("endDate") Date endDate,
                                               @Param("minPrice") BigDecimal minPrice,
                                               @Param("maxPrice") BigDecimal maxPrice);


    @Query("SELECT u FROM SubMaterials u WHERE u.subMaterialName = :query AND u.material.materialName = :materialName")
    SubMaterials findBySubmaterialNameAndMaterialName(@Param("query") String subMaterialName, @Param("materialName") String materialName);

    @Query("SELECT s FROM SubMaterials s" +
            " LEFT JOIN (" +
            "SELECT ism.subMaterials.subMaterialId as subMaterialId, ism.quantity as total_quantity, ism.out_price as out_price, ism.input_price as input_price" +
            " FROM InputSubMaterial ism " +
            "ORDER BY ism.date_input DESC " +
            "LIMIT 1 " +
            ") latestInput ON s.subMaterialId = latestInput.subMaterialId " +
            " WHERE s.subMaterialName = :query AND s.material.materialName = :materialName AND latestInput.out_price = :unitPrice")
    SubMaterials findBySubmaterialNameAndMaterialNameAndPrice(@Param("query") String subMaterialName, @Param("materialName") String materialName,@Param("unitPrice") BigDecimal unitPrice);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, '')," +
            " COALESCE(m.materialName, '')," +

            "ism.quantity, ism.out_price, ism.input_price, m.type,s.code,ism.input_id) " +

            "FROM SubMaterials s " +
            "LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển LEFT JOIN s.material m đến đây
            "WHERE ism.input_id = ("  +
            " SELECT MAX(ism2.input_id)" +
            "FROM InputSubMaterial ism2" +
            " WHERE ism2.code_input = ism.code_input)")
    List<SubMaterialViewDTO> getAllSubmaterial();


    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, '')," +

            "ism.quantity, ism.out_price, ism.input_price,m.type,ism.code_input,ism.input_id) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID

            "FROM SubMaterials s " +
            "LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE m.materialId = :query AND " +
            "ism.input_id = ("  +
            " SELECT MAX(ism2.input_id)" +
            "FROM InputSubMaterial ism2" +
            " WHERE ism2.code_input = ism.code_input)")
    List<SubMaterialViewDTO> findSubMaterialIdByMaterial(int query);


    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), " +

            "COALESCE(m.materialName, ''), ism.quantity, ism.out_price, ism.input_price,m.type,s.code,ism.input_id) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID

            "FROM SubMaterials s " +
            "LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE ism.input_id = ( " +
            "         SELECT MAX(ism2.input_id)" +
            "        FROM InputSubMaterial ism2" +
            "         WHERE ism2.code_input = ism.code_input) AND s.subMaterialName LIKE CONCAT('%', :keyword, '%') OR " +
            "s.code LIKE CONCAT('%', :keyword, '%')")
    List<SubMaterialViewDTO> findSubMaterialsByNameCode(@Param("keyword") String keyword);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), " +

            "COALESCE(m.materialName, ''), ism.quantity, ism.out_price, ism.input_price,m.type,s.code,ism.input_id) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID

            "FROM SubMaterials s " +
            "LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE " +
            "ism.input_id = ("  +
            " SELECT MAX(ism2.input_id)" +
            "FROM InputSubMaterial ism2" +
            " WHERE ism2.code_input = ism.code_input) AND s.subMaterialId = :subMaterialId")
    SubMaterialViewDTO findSubMaterialsById(int subMaterialId);


//    @Query("SELECT u FROM SubMaterials u WHERE u.material.materialId = :query")
//    List<SubMaterials> findSubMaterialIdByMaterial(int query);

    List<SubMaterials> findAll();
    int countBySubMaterialName(String SubMaterialName);

    @Query(value = "SELECT p.* FROM sub_materials p WHERE p.code LIKE :prefix% ORDER BY p.code DESC LIMIT 1", nativeQuery = true)
    SubMaterials findSubMaterialsTop(@Param("prefix") String prefix);


    @Query("SELECT ism.quantity FROM SubMaterials s" +
            " LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE (ism.date_input, ism.input_id) IN ( " +
            "   SELECT MAX(ism2.date_input), MAX(ism2.input_id) " +
            "   FROM InputSubMaterial ism2 " +
            "   WHERE ism2.subMaterials.subMaterialId = s.subMaterialId " +
            "   GROUP BY ism2.subMaterials.subMaterialId ) AND s.subMaterialId = :query")
    Integer findQuantityBySubMaterialId(int query);




//    @Query("SELECT u FROM SubMaterials u  WHERE u.subMaterialName LIKE CONCAT('%', :keyword, '%') OR " +
//            "u.code LIKE CONCAT('%', :keyword, '%')")
//    List<SubMaterials> findSubMaterialsByNameCode(@Param("keyword") String keyword);


//    @Transactional
//    @Modifying
//    @Query("update SubMaterials u set u.subMaterialName = ?2,u.description=?3,u.quantity=?4,u.unitPrice=?5" +
//            " where u.subMaterialId = ?1")
//    void updateSubMaterials(int productId, String subMaterialName, String description, Double quantity, BigDecimal unitPrice);








}
