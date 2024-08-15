package com.example.demo.Repository;

import com.example.demo.Entity.InputSubMaterial;
import com.example.demo.Entity.SubMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface InputSubMaterialRepository extends JpaRepository<InputSubMaterial,Integer> {

    //lấy giá submaterial đã cập nhật lần cuối(bản ghi mới nhất) (loại action là cạp nhật giá)
    @Query("SELECT ism FROM InputSubMaterial ism WHERE ism.subMaterials.subMaterialId = :subMaterialId AND ism.actionType.action_type_id = 4  ORDER BY ism.date_input DESC LIMIT 1   ")
    InputSubMaterial findLatestInputSubMaterialBySubMaterialId(@Param("subMaterialId") int subMaterialId);

    @Query("SELECT u FROM InputSubMaterial u WHERE u.input_id = :query")
    InputSubMaterial findById(int query);

    @Query("SELECT u.code_input FROM InputSubMaterial u WHERE u.input_id = :query")
    String findCodeInputById(int query);

    @Query("SELECT s.material.materialId FROM InputSubMaterial u LEFT JOIN u.subMaterials s WHERE u.input_id = :query")
    Integer findMaterialInputById(int query);

    @Query(value = "SELECT p.* FROM input_sub_material p WHERE p.code_input LIKE :prefix% ORDER BY p.code_input DESC LIMIT 1", nativeQuery = true)
    InputSubMaterial findInputSubMaterialTop(@Param("prefix") String prefix);



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

    @Query("SELECT ism " +
            "FROM InputSubMaterial ism " +
            "LEFT JOIN ism.subMaterials s  " +
            " LEFT JOIN ism.actionType a"+
            " WHERE " +
            " ism.input_id = ("  +
            " SELECT MAX(ism2.input_id)" +
            "FROM InputSubMaterial ism2" +
            " WHERE ism2.code_input = ism.code_input) AND ism.code_input = :code_input AND s.subMaterialId = :subMaterialId ")
    InputSubMaterial findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(@Param("code_input") String code_input,
            @Param("subMaterialId") int subMaterialId);


//    @Query("SELECT ism " +
//            "FROM InputSubMaterial ism " +
//            "LEFT JOIN SubMaterials s ON ism.subMaterials.subMaterialId = s.subMaterialId " +
//            " JOIN Action_Type a ON ism.actionType.action_type_id = a.action_type_id " +
//            "WHERE a.action_type_id = 1 " +
//            "AND ism.input_id = ( " +
//            "  SELECT MAX(ism2.date_input) " +
//            "  FROM InputSubMaterial ism2 " +
//            "  WHERE ism2.code_input = ism.code_input) " +
//            "AND ism.code_input = :code_input " +
//            "AND s.subMaterialId = :subMaterialId ")
//    InputSubMaterial findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCodeTest(@Param("code_input") String code_input,
//                                                                                         @Param("subMaterialId") int subMaterialId);

    @Query(value = "SELECT * FROM input_sub_material WHERE action_type_id = 1 AND code_input = :codeInput AND sub_material_id = :subMaterialId ORDER BY input_id DESC LIMIT 1", nativeQuery = true)
    InputSubMaterial findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCodeTest(@Param("codeInput") String codeInput, @Param("subMaterialId") Integer subMaterialId);

//    @Query(value = "SELECT p.* FROM input_sub_material p WHERE p.code_input LIKE :prefix% AND p.action_type_id ORDER BY p.code_input DESC LIMIT 1", nativeQuery = true)
//    InputSubMaterial findInputSubMaterialTop(@Param("prefix") String prefix);


//    @Query("SELECT ism " +
//            "FROM InputSubMaterial ism " +
//            "WHERE ism.subMaterials.subMaterialId = :subMaterialId " +
//            "AND (ism.date_input, ism.input_id) = (" +
//            "  SELECT MAX(i.date_input), MAX(i.input_id) " +
//            "  FROM InputSubMaterial i " +
//            "  WHERE i.subMaterials.subMaterialId = ism.subMaterials.subMaterialId " +
//            "  AND i.code_input = ism.code_input)")
//    InputSubMaterial findLatestSubMaterialInputSubMaterial(@Param("code_input") String code_input, @Param("subMaterialId") int subMaterialId);



    @Query("SELECT ism"+
           " FROM InputSubMaterial ism " +
          " WHERE " +
            "ism.input_id = ("  +
          " SELECT MAX(ism2.input_id)" +
           "FROM InputSubMaterial ism2" +
           " WHERE ism2.code_input = ism.code_input)")
    List<InputSubMaterial> findAllInputSubMaterialsOrderByCodeAndDate();






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
    @Query("SELECT SUM(a.quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 2 ")
    BigDecimal findTotalInputSubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);

    //số tiền chênh lệch lúc cập nhật số lượng theo tháng của mõi sản phẩm , nếu bigdecimal mà duogw nghĩa là thêm sản phẩm vào kho, , phải sử dụng dấu +
    @Query("SELECT SUM(a.quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 3" +
            " AND a.quantity >= 0 ")
    BigDecimal findTotalEditQuantitySubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(a.quantity*a.out_price) FROM InputSubMaterial a  WHERE MONTH(a.date_input) = :month AND YEAR(a.date_input) = :year AND a.actionType.action_type_id = 5" +
            " AND a.quantity >= 0 ")
    BigDecimal findTotalEditQuantityAndPriceSubMaterialByMonthAndYear(@Param("month") int month, @Param("year") int year);



}
