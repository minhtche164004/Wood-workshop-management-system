package com.example.demo.Repository;

import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@EnableJpaRepositories
@Repository
public interface RequestProductsSubmaterialsRepository extends JpaRepository<RequestProductsSubmaterials,Integer> {

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.subMaterial.subMaterialId = :query")
    List<RequestProductsSubmaterials> findBySubMaterialId(int query);

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query")
    List<RequestProductsSubmaterials> findByRequestProductID(int query);

    @Transactional
    @Modifying
    @Query("DELETE FROM RequestProductsSubmaterials u WHERE u.requestProductsSubmaterialsId = :query")
    void deleteRequestProductSubMaterialsById(@Param("query") int requestProductsSubmaterialsId);

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query  AND u.subMaterial.material.materialId = :mate_id")
    List<RequestProductsSubmaterials> findByRequestProductIDAndMate(int query,int mate_id);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO(j.requestProductsSubmaterialsId, sub.subMaterialName,sub.subMaterialId,j.quantity, m.type) " +
            "FROM RequestProductsSubmaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN sub.material m WHERE j.requestProduct.requestProductId= :requestProductId AND j.subMaterial.material.materialId = :materialId")
    List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByProductIdAndTypeMate(int requestProductId,int materialId);

    @Query("SELECT u.subMaterial.subMaterialName FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query AND u.subMaterial.material.materialId IN (1, 2)")
    List<String> GetSubNameByProductId(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO( " +
            "m.materialId ,sub.subMaterialId ,sub.subMaterialName, m.type, ism.out_price, j.quantity,ism.code_input,ism.input_id) " +
            "FROM RequestProductsSubmaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN j.inputSubMaterial ism " +
            " LEFT JOIN sub.material m " + // Di chuyển điều kiện WHERE vào đây
            "WHERE j.requestProduct.requestProductId = :requestProductId")
    List<SubMateProductRequestDTO> getRequestProductSubMaterialByRequestProductIdDTO(int requestProductId);

//    @Query("SELECT SUM(latestInput.total_quantity * latestInput.out_price) AS total FROM (" +
//            "SELECT ism.subMaterials.subMaterialId, ism.total_quantity, ism.out_price, ism.input_price " +
//            "FROM InputSubMaterial ism " +
//            "ORDER BY ism.date_input DESC " +
//            "LIMIT 1 " +
//            ") latestInput")
//    BigDecimal totalAmountSubMaterial();

}
