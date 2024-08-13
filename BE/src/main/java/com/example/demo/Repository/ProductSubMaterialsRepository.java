package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Entity.WishList;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductSubMaterialsRepository extends JpaRepository<ProductSubMaterials,Integer> {

    @Query("SELECT u.subMaterial.subMaterialName FROM ProductSubMaterials u WHERE u.product.productId = :query AND u.subMaterial.material.materialId IN (1, 2)")
    List<String> GetSubNameByProductId(int query);

//    @Query("SELECT u.subMaterial FROM ProductSubMaterials u WHERE u.product.productId = :query AND u.subMaterial.material.materialId IN (1, 2)")
//    List<SubMaterials> GetSubMaterialByProductId(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), " +
            "COALESCE(m.materialName, ''), ism.quantity, ism.out_price,ism.input_price,m.type,s.code,ism.input_id) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM ProductSubMaterials p " +
            "LEFT JOIN p.subMaterial s " +
            "LEFT JOIN InputSubMaterial ism ON s.subMaterialId = ism.subMaterials.subMaterialId" +
            " LEFT JOIN s.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE " +
            "ism.input_id = ("  +
            " SELECT MAX(ism2.input_id)" +
            "FROM InputSubMaterial ism2" +
            " WHERE ism2.code_input = ism.code_input) AND  p.product.productId = :query AND p.subMaterial.material.materialId IN (1, 4)")
    List<SubMaterialViewDTO> GetSubMaterialByProductId(int query);

    @Query("SELECT p.product " +
            "FROM ProductSubMaterials p " +
            "JOIN p.subMaterial s " +
            "WHERE s.subMaterialId = :query")
    List<Products> getProductIdsBySubMaterialId(@Param("query") int subMaterialId);

    @Query("SELECT p.quantity " +
            "FROM ProductSubMaterials p " +
            "LEFT JOIN p.product pr " +
            "JOIN p.subMaterial s " +
            "WHERE pr.productId = :productId AND s.subMaterialId = :subMaterialId")
    Double getQuantityInProductSubMaterialsByProductId(@Param("productId") int productId,@Param("subMaterialId") int subMaterialId);



    @Transactional
    @Modifying
    @Query("DELETE FROM ProductSubMaterials u WHERE u.productSubMaterialId = :query")
    void deleteProductSubMaterialsById(@Param("query") int productSubMaterialId);

    @Query("SELECT u FROM ProductSubMaterials u WHERE u.product.productId = :query AND u.subMaterial.material.materialId = :mate_id")
    List<ProductSubMaterials> findByProductIDAndMate(int query,int mate_id);

    @Query("SELECT u FROM ProductSubMaterials u WHERE u.product.productId = :query")
    List<ProductSubMaterials> findByProductID(int query);

    @Query("SELECT u FROM ProductSubMaterials u WHERE u.subMaterial.subMaterialId = :query")
    List<ProductSubMaterials> findBySubMaterialId(int query);


    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO(j.productSubMaterialId, sub.subMaterialName,sub.subMaterialId,j.quantity, m.type) " +
            "FROM ProductSubMaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN sub.material m WHERE j.product.productId= :productId AND j.subMaterial.material.materialId = :materialId")
    List<Product_SubmaterialDTO> getProductSubMaterialByProductIdAndTypeMate(int productId,int materialId);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO( " +
            "m.materialId ,sub.subMaterialId ,sub.subMaterialName, m.type, ism.out_price, j.quantity,ism.code_input,ism.input_id) " +
            "FROM ProductSubMaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN j.inputSubMaterial ism " +
            " LEFT JOIN sub.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE j.product.productId = :productId")
    List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId);


    @Query("SELECT SUM(s.quantity*latestInput.out_price) FROM ProductSubMaterials s " +
            " LEFT JOIN (" +
            "SELECT ism.subMaterials.subMaterialId as subMaterialId, ism.quantity as total_quantity, ism.out_price as out_price, ism.input_price as input_price" +
            " FROM InputSubMaterial ism " +
            "ORDER BY ism.date_input DESC " +
            "LIMIT 1 " +
            ") latestInput ON s.subMaterial.subMaterialId = latestInput.subMaterialId " +
            " LEFT JOIN s.subMaterial sub WHERE s.product.productId = :productId")
    BigDecimal ToTalProductSubMaterialByProductId(int productId);

//    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO( " +
//            "CAST(m.materialId AS string), CAST(sub.subMaterialId AS string),sub.subMaterialName, m.type, sub.unitPrice, j.quantity) " +
//            "FROM ProductSubMaterials j " +
//            "LEFT JOIN j.subMaterial sub " +
//            "LEFT JOIN sub.material m " +
//            "WHERE j.product.productId = :productId")
//    List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId);






}
