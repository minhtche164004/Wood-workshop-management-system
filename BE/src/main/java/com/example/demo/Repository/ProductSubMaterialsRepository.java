package com.example.demo.Repository;

import com.example.demo.Dto.OrderDTO.JobProductDTO;
import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface ProductSubMaterialsRepository extends JpaRepository<ProductSubMaterials,Integer> {

    @Query("SELECT u.subMaterial.subMaterialName FROM ProductSubMaterials u WHERE u.product.productId = :query")
    List<String> GetSubNameByProductId(int query);

    @Query("SELECT u FROM ProductSubMaterials u WHERE u.product.productId = :query")
    List<ProductSubMaterials> findByProductID(int query);

    @Query("SELECT u FROM ProductSubMaterials u WHERE u.subMaterial.subMaterialId = :query")
    List<ProductSubMaterials> findBySubMaterialId(int query);


    @Query("SELECT new com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO(j.productSubMaterialId, sub.subMaterialName,sub.subMaterialId,j.quantity, m.type) " +
            "FROM ProductSubMaterials j " +
            "LEFT JOIN j.subMaterial sub " +
            "LEFT JOIN sub.material m WHERE j.product.productId= :productId")
    List<Product_SubmaterialDTO> getProductSubMaterialByProductId(int productId);


}
