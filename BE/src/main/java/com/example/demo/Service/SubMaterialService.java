package com.example.demo.Service;

//import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.ProductDTO.QuantityTotalDTO;
import com.example.demo.Dto.SubMaterialDTO.*;
import com.example.demo.Entity.Employeematerials;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface SubMaterialService {
    List<SubMaterials> getAll();
    List<SubMaterials> FilterByMaterial(int material_id);
    SubMaterials addNew(SubMaterialDTO subMaterialDTO);
    List<SubMaterialNameDTO> GetListName();
    void saveSubMaterialToDatabase(MultipartFile file);
    List<SubMaterials> SearchByNameorCode(String key);
    UpdateSubDTO UpdateSub(int id, UpdateSubDTO updateSubDTO);


    List<ProductSubMaterials> createExportMaterialProduct(int product_id, Map<Integer, Double> subMaterialQuantities);
    ResponseEntity<ApiResponse<List<String>>> createExportMaterialProductTotalJob(int product_id,int mate_id, QuantityTotalDTO quantityTotalDTO,int emp_id);
    ResponseEntity<ApiResponse<List<String>>> createExportMaterialRequestTotalJob(int product_id,int mate_id,QuantityTotalDTO quantityTotalDTO,int emp_id);
    //xuất nguyên liệu cho sản phẩm  theo yêu cầu
    List<RequestProductsSubmaterials> createExportMaterialProductRequest(int request_product_id, Map<Integer, Double> subMaterialQuantities);
    List<Product_SubmaterialDTO> getProductSubMaterialByProductId(int id,int material_id);
    List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByRequestProductId(int id,int material_id);

   // List<Employeematerials> createEMaterial(int emp_id,int mate_id,int product_id);
    List<Employeematerials> getAllEmpMate();

    List<Employeematerials> findEmployeematerialsByName(String key);
//    List<Employeematerials> filterEmployeematerialsByMaterialType(int materialId);

}
