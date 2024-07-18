package com.example.demo.Service;

//import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.ProductDTO.QuantityTotalDTO;
import com.example.demo.Dto.SubMaterialDTO.*;
import com.example.demo.Entity.Employeematerials;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.Impl.ExcelError;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
public interface SubMaterialService {
    List<SubMaterialViewDTO> getAll();
    List<SubMaterialViewDTO> FilterByMaterial(int material_id);
    SubMaterials addNew(SubMaterialDTO subMaterialDTO);
    List<SubMaterialNameDTO> GetListName();
    List<ExcelError> saveSubMaterialToDatabase(MultipartFile file);
    List<SubMaterialViewDTO> SearchByNameorCode(String key);
    UpdateSubDTO UpdateSub(int id, UpdateSubDTO updateSubDTO);
    SubMaterialViewDTO getSubMaterialById(int sub_material_id);


    List<ProductSubMaterials> createExportMaterialProduct(int product_id, Map<Integer, Double> subMaterialQuantities);
    ResponseEntity<ApiResponse<List<String>>> createExportMaterialProductTotalJob(int product_id,int mate_id, QuantityTotalDTO quantityTotalDTO,int emp_id);
    ResponseEntity<ApiResponse<List<String>>> createExportMaterialRequestTotalJob(int product_id,int mate_id,QuantityTotalDTO quantityTotalDTO,int emp_id);
    //xuất nguyên liệu cho sản phẩm  theo yêu cầu
    List<List<RequestProductsSubmaterials>> createExportMaterialProductRequest(List<Integer> request_product_id, List<Map<Integer, Double>> subMaterialQuantities);
    List<Product_SubmaterialDTO> getProductSubMaterialByProductId(int id,int material_id);
    List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByRequestProductId(int id,int material_id);

   // List<Employeematerials> createEMaterial(int emp_id,int mate_id,int product_id);
    List<Employee_MaterialDTO> getAllEmpMate();

    List<Employee_MaterialDTO> findEmployeematerialsByName(String key);

    SubMaterialViewDTO EditSubMaterial(int id,SubMaterialViewDTO subMaterialViewDTO);

//    List<Employeematerials> filterEmployeematerialsByMaterialType(int materialId);

    List<ProductSubMaterials> EditSubMaterialProduct(int product_id, Map<Integer, Double> subMaterialQuantities);
    List<RequestProductsSubmaterials> EditSubMaterialRequestProduct(int request_product_id, Map<Integer, Double> subMaterialQuantities);

}
