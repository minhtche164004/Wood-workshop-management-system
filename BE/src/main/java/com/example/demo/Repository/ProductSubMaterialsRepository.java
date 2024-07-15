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

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductSubMaterialsRepository extends JpaRepository<ProductSubMaterials,Integer> {

    @Query("SELECT u.subMaterial.subMaterialName FROM ProductSubMaterials u WHERE u.product.productId = :query AND u.subMaterial.material.materialId IN (1, 2)")
    List<String> GetSubNameByProductId(int query);

//    @Query("SELECT u.subMaterial FROM ProductSubMaterials u WHERE u.product.productId = :query AND u.subMaterial.material.materialId IN (1, 2)")
//    List<SubMaterials> GetSubMaterialByProductId(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO(" +
            "s.subMaterialId, COALESCE(s.subMaterialName, ''), m.materialId, COALESCE(s.description, ''), COALESCE(m.materialName, ''), s.quantity, s.unitPrice,m.type) " + // Thêm dấu phẩy và loại bỏ COALESCE cho các ID
            "FROM ProductSubMaterials p " +
            "LEFT JOIN p.subMaterial s " +
            "LEFT JOIN s.material m WHERE  p.product.productId = :query AND p.subMaterial.material.materialId IN (1, 4)")
    List<SubMaterialViewDTO> GetSubMaterialByProductId(int query);

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
            "m.materialId ,sub.subMaterialId ,sub.subMaterialName, m.type, sub.unitPrice, j.quantity) " +
            "FROM ProductSubMaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN sub.material m " +
            "WHERE j.product.productId = :productId")
    List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId);

//    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO( " +
//            "CAST(m.materialId AS string), CAST(sub.subMaterialId AS string),sub.subMaterialName, m.type, sub.unitPrice, j.quantity) " +
//            "FROM ProductSubMaterials j " +
//            "LEFT JOIN j.subMaterial sub " +
//            "LEFT JOIN sub.material m " +
//            "WHERE j.product.productId = :productId")
//    List<SubMateProductDTO> getProductSubMaterialByProductIdDTO(int productId);






}
