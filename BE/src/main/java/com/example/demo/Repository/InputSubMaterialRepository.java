package com.example.demo.Repository;

import com.example.demo.Entity.InputSubMaterial;
import com.example.demo.Entity.SubMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@EnableJpaRepositories
public interface InputSubMaterialRepository extends JpaRepository<InputSubMaterial,Integer> {

    //lấy giá submaterial đã cập nhật lần cuối(bản ghi mới nhất) (loại action là cạp nhật giá)
    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId AND ism.actionType.action_type_id = 4  ORDER BY ism.date_input DESC LIMIT 1   ")
    InputSubMaterial findLatestInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);

    @Query("SELECT ism FROM InputSubMaterial ism " +
            "WHERE ism.subMaterials.subMaterialId = :subMaterialId " +
            "AND ism.date_input = ( " +
            "   SELECT MAX(ism2.date_input) " +
            "   FROM InputSubMaterial ism2 " +
            "   WHERE ism2.subMaterials.subMaterialId = ism.subMaterials.subMaterialId " +
            ") " +
            "AND ism.input_id = ( " +
            "   SELECT MAX(ism3.input_id) " +
            "   FROM InputSubMaterial ism3 " +
            "   WHERE ism3.subMaterials.subMaterialId = ism.subMaterials.subMaterialId " +
            "   AND ism3.date_input = ism.date_input " +
            ")")
    InputSubMaterial findLatestSubMaterialInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);


//
//    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId  ORDER BY ism.date_input DESC LIMIT 1   ")
//    InputSubMaterial findLatestSubMaterialInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);

    //lấy quantiy submaterial đã cập nhật lần cuối(bản ghi mới nhất) (loại action là cạp nhật giá)
    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId AND ism.actionType.action_type_id = 3  ORDER BY ism.date_input DESC LIMIT 1   ")
    InputSubMaterial findLatestQuantityInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);


    //lấy giá submaterial đã cập nhật lần cuối(bản ghi mới nhất) (loại action là cạp nhật giá)(giá bán)
    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId AND ism.actionType.action_type_id = 4  ORDER BY ism.date_input DESC LIMIT 1   ")
    InputSubMaterial findLatestOutputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);

    //số tiền nhập kho theo tháng của mõi sản phẩm
    @Query("SELECT SUM(a.total_quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 2 ")
    BigDecimal findTotalInputSubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);

    //số tiền chênh lệch lúc cập nhật số lượng theo tháng của mõi sản phẩm , nếu bigdecimal mà duogw nghĩa là thêm sản phẩm vào kho, , phải sử dụng dấu +
    @Query("SELECT SUM(a.total_quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 3" +
            " AND a.total_quantity >= 0 ")
    BigDecimal findTotalEditQuantitySubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(a.total_quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 5" +
            " AND a.total_quantity >= 0 ")
    BigDecimal findTotalEditQuantityAndPriceSubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);



}
