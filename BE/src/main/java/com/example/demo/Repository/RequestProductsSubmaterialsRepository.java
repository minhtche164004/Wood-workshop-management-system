package com.example.demo.Repository;

import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
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

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.subMaterial = :query")
    List<RequestProductsSubmaterials> findBySubMaterialId(int query);

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query")
    List<RequestProductsSubmaterials> findByRequestProductID(int query);

//    @Query("SELECT u FROM RequestProductsSubmaterials u LEFT JOIN u.inputSubMaterial.subMaterials.material m WHERE u.requestProduct.requestProductId = :query" +
//            " AND m.materialId = :materialId")
//    List<RequestProductsSubmaterials> findBySubMateRequestProductID(int query,int materialId);

//    @Query(value = "SELECT * FROM request_products_submaterials u " +
//            "LEFT JOIN request_products r ON u.request_product_id = r.request_product_id " +
//            "LEFT JOIN input_sub_material i ON u.input_id = i.input_id " +
//            "WHERE r.request_product_id = :requestProductId AND i.input_id = :input_iD", nativeQuery = true)
//   RequestProductsSubmaterials findByRequestProductIdAndInputId(@Param("requestProductId") int requestProductId, @Param("input_iD") int input_iD);

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query  AND u.inputSubMaterial.input_id = :input_id")
    RequestProductsSubmaterials findByRequestProductIdAndInputId(int query,int input_id);

    @Query("SELECT u FROM RequestProductsSubmaterials u LEFT JOIN u.inputSubMaterial.subMaterials s WHERE u.subMaterial = :query  AND u.inputSubMaterial.input_id = :input_id")
    SubMaterials findBySubMaterialByIdAndInputId(int query, int input_id);

//    @Transactional
//    @Modifying
//    @Query("update RequestProductsSubmaterials u set u.quantity = ?2 where u.requestProduct.requestProductId = ?1 and u.inputSubMaterial.input_id = ?3")
//    void updatequantity(int requestProductId, double quantity,int input_id);

    @Transactional
    @Modifying
    @Query(value = "update RequestProductsSubmaterials u set u.quantity = :quantity where u.requestProduct.requestProductId = :requestProductId and u.inputSubMaterial.input_id = :inputId", nativeQuery = false)
    void updateQuantity(@Param("requestProductId") int requestProductId, @Param("quantity") double quantity, @Param("inputId") int inputId);

    //    @Transactional
    @Modifying
    @Query("DELETE FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :requestProductId  " +
            " AND u.inputSubMaterial.input_id = :input_id ")
    void deleteRequestProductSubMaterialsByIdTest(@Param("requestProductId") int requestProductId,@Param("input_id") int input_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM RequestProductsSubmaterials u WHERE u.requestProductsSubmaterialsId = :query")
    void deleteRequestProductSubMaterialsById(@Param("query") int requestProductsSubmaterialsId);

    @Query("SELECT u FROM RequestProductsSubmaterials u LEFT JOIN u.inputSubMaterial.subMaterials.material m WHERE u.requestProduct.requestProductId = :query  AND m.materialId = :mate_id")
    List<RequestProductsSubmaterials> findByRequestProductIDAndMate(int query,int mate_id);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO(j.requestProductsSubmaterialsId, sub.subMaterialName,sub.subMaterialId" +
            ",j.quantity, m.type,i.code_input) " +
            "FROM RequestProductsSubmaterials j " +
            "LEFT JOIN j.inputSubMaterial.subMaterials sub"+
          //  "LEFT JOIN j.subMaterial sub" +
            " LEFT JOIN j.inputSubMaterial i" +
            " LEFT JOIN sub.material m WHERE j.requestProduct.requestProductId= :requestProductId AND sub.material.materialId = :materialId")
    List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByProductIdAndTypeMate(int requestProductId,int materialId);

    @Query("SELECT sub.subMaterialName FROM RequestProductsSubmaterials u  LEFT JOIN u.inputSubMaterial.subMaterials sub WHERE u.requestProduct.requestProductId = :query AND sub.material.materialId IN (1, 2)")
    List<String> GetSubNameByProductId(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.SubMateProductRequestDTO( " +
            "m.materialId ,sub.subMaterialId ,sub.subMaterialName, m.type, ism.out_price, j.quantity,ism.code_input,ism.input_id) " +
            "FROM RequestProductsSubmaterials j " +
            "LEFT JOIN j.inputSubMaterial.subMaterials sub "+
            " LEFT JOIN j.inputSubMaterial ism " +
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
