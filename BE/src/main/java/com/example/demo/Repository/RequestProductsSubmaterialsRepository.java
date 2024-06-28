package com.example.demo.Repository;

import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;
@EnableJpaRepositories
@Repository
public interface RequestProductsSubmaterialsRepository extends JpaRepository<RequestProductsSubmaterials,Integer> {

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.subMaterial.subMaterialId = :query")
    List<RequestProductsSubmaterials> findBySubMaterialId(int query);

    @Query("SELECT u FROM RequestProductsSubmaterials u WHERE u.requestProduct.requestProductId = :query")
    List<RequestProductsSubmaterials> findByRequestProductID(int query);

    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.ReProduct_SubmaterialDTO(j.productSubMaterialId, sub.subMaterialName,sub.subMaterialId,j.quantity, m.type) " +
            "FROM ProductSubMaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN sub.material m WHERE j.product.productId= :productId")
    List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByProductId(int productId);
}
