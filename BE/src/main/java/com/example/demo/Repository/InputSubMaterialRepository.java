package com.example.demo.Repository;

import com.example.demo.Entity.InputSubMaterial;
import com.example.demo.Entity.SubMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface InputSubMaterialRepository extends JpaRepository<InputSubMaterial,Integer> {

    //lấy giá submaterial đã cập nhật lần cuối(bản ghi mới nhất)
    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId ORDER BY ism.date_input DESC LIMIT 1   ")
    InputSubMaterial findLatestInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);

}
