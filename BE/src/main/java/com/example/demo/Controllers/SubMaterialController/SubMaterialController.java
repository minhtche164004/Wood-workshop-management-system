package com.example.demo.Controllers.SubMaterialController;

//import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.ProductDTO.CreateExportMaterialProductRequest;
import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.ProductDTO.QuantityTotalDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Dto.SubMaterialDTO.UpdateSubDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.ProductService;
import com.example.demo.Service.SubMaterialService;
import io.jsonwebtoken.io.IOException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/submaterial/")
@AllArgsConstructor
public class SubMaterialController {
    @Autowired
    private SubMaterialService subMaterialService;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/getall")
    public ApiResponse<?> getAllSubMaterials() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.getAll());
        return apiResponse;
    }
    @GetMapping("/getSubmaterialById")
    public ApiResponse<?> getSubmaterialById(@RequestParam("id") int id) {
        ApiResponse<SubMaterialViewDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.getSubMaterialById(id));
        return apiResponse;
    }
    @PutMapping("/editSubMaterial")
    public ApiResponse<?> editSubMaterial(@RequestParam("id") int id,@RequestBody SubMaterialViewDTO subMaterialViewDTO) {
        ApiResponse<SubMaterialViewDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.EditSubMaterial(id,subMaterialViewDTO));
        return apiResponse;
    }

    @PostMapping("/AddNewSubMaterial")
    public ApiResponse<?> addNewSubMaterial(@RequestBody @Valid SubMaterialDTO subMaterialDTO) {
        ApiResponse<SubMaterials> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.addNew(subMaterialDTO));
        return apiResponse;
    }
    @GetMapping("/getAllName")
    public ApiResponse<?> getAllSubMaterialsName() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.GetListName());
        return apiResponse;
    }
    @GetMapping("/FilterByMaterial")
    public ApiResponse<?> FilterByMaterial(@RequestParam("id") int material_id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.FilterByMaterial(material_id));
        return apiResponse;
    }

    @GetMapping("/UpdateSubMaterial")
    public ApiResponse<?> UpdateSubMaterial(@RequestParam("id") int id, @RequestBody @Valid UpdateSubDTO updateSubDTO) {
        ApiResponse<UpdateSubDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.UpdateSub(id,updateSubDTO));
        return apiResponse;
    }


    @GetMapping("/SearchByNameorCode")
    public ApiResponse<?> SearchByNameorCode(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.SearchByNameorCode(key));
        return apiResponse;
    }
    @PutMapping("/EditSubMaterialProduct")
    public ApiResponse<?> EditSubMaterialProduct(@RequestBody CreateExportMaterialProductRequest request) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.EditSubMaterialProduct(request.getProductId(), request.getSubMaterialQuantities()));
        return apiResponse;
    }
    @PutMapping("/EditSubMaterialRequestProduct")
    public ApiResponse<?> EditSubMaterialRequestProduct(@RequestBody CreateExportMaterialProductRequest request) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.EditSubMaterialRequestProduct(request.getProductId(), request.getSubMaterialQuantities()));
        return apiResponse;
    }

    @PostMapping("/upload-submaterial-data")
    public ApiResponse<?> uploadCustomersData(@RequestParam("file") MultipartFile file) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        subMaterialService.saveSubMaterialToDatabase(file);
        apiResponse.setResult("Đọc file thành công , dữ liệu đã đưọc thêm vào ");
        return apiResponse;
    }


    @GetMapping("/download-form-submaterial-data-excel")
    public ResponseEntity<Resource> downloadFile() {
        try {
            // 1. Sử dụng ResourceLoader để lấy resource từ classpath
            Resource resource = resourceLoader.getResource("classpath:templates/Bieu mau nhap nguyen lieu.xlsx");

            // 2. Kiểm tra xem resource có tồn tại không
            if (!resource.exists()) {
                throw new RuntimeException("File not found");
            }

            // 3. Tạo header
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");

            // 4. Trả về ResponseEntity
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .headers(headers)
                    .body(resource);

        } catch (IOException ex) {
            throw new RuntimeException("Error: " + ex.getMessage());
        }
    }

    @GetMapping("/getProductSubMaterialByProductId")
    public ApiResponse<?> getProductSubMaterialByProductId(@RequestParam("id") int id,@RequestParam("mate_id") int mate_id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.getProductSubMaterialByProductId(id,mate_id));
        return apiResponse;
    }

    @GetMapping("/getRequestProductSubMaterialByRequestProductId")
    public ApiResponse<?> getRequestProductSubMaterialByRequestProductId(@RequestParam("id") int id,@RequestParam("mate_id") int mate_id) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.getRequestProductSubMaterialByRequestProductId(id,mate_id));
        return apiResponse;
    }
    //xuất đơn nguyên vật liệu cho product có sẵn
    // public ResponseEntity<ApiResponse<List<ProductSubMaterials>>> createExportMaterialProduct(@RequestBody CreateExportMaterialProductRequest request) {
    @PostMapping("/createExportMaterialProduct")
    public List<ProductSubMaterials> createExportMaterialProduct(@RequestBody CreateExportMaterialProductRequest request) {
        return subMaterialService.createExportMaterialProduct(request.getProductId(), request.getSubMaterialQuantities());
    }

    //xuất đơn vật liệu cho đơn hàng đặt theo yêu cầu , request product
    @PostMapping("/createExportMaterialProductRequest")
    public List<RequestProductsSubmaterials> createExportMaterialProductRequest(@RequestBody CreateExportMaterialProductRequest request) {
        return subMaterialService.createExportMaterialProductRequest(request.getProductId(), request.getSubMaterialQuantities());
    }

    @PostMapping("/createExportMaterialProductTotalJob")
    public ResponseEntity<ApiResponse<List<String>>> createExportMaterialProductTotalJob(@RequestParam("id") int id ,@RequestParam("mate_id") int mate_id, @RequestBody QuantityTotalDTO quantityTotalDTO
    ,@RequestParam("emp_id") int emp_id) {
        return subMaterialService.createExportMaterialProductTotalJob(id,mate_id,quantityTotalDTO,emp_id);
    }

    @PostMapping("/createExportMaterialRequestTotalJob")
    public ResponseEntity<ApiResponse<List<String>>> createExportMaterialRequestTotalJob(@RequestParam("id") int id ,@RequestParam("mate_id") int mate_id, @RequestBody QuantityTotalDTO quantityTotalDTO
            ,@RequestParam("emp_id") int emp_id) {
        return subMaterialService.createExportMaterialRequestTotalJob(id,mate_id,quantityTotalDTO,emp_id);
    }

//    @PostMapping("/CreateEMaterial")
//    public ApiResponse<?> CreateEMaterial(@RequestParam("product_id") int product_id ,@RequestParam("mate_id") int mate_id,@RequestParam("emp_id") int emp_id) {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(subMaterialService.createEMaterial(emp_id, mate_id,product_id));
//        return apiResponse;
//    }
    @GetMapping("/getAllEmpMate")
    public ApiResponse<?> getAllEmpMate() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.getAllEmpMate());
        return apiResponse;
    }

    @GetMapping("/findEmployeematerialsByNameEmployee")
    public ApiResponse<?> findEmployeematerialsByName(@RequestParam("key") String key) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.findEmployeematerialsByName(key));
        return apiResponse;
    }

//    @GetMapping("/filterEmployeematerials")
//    public ApiResponse<?> filterEmployeematerialsByMaterialType(@RequestParam("mate_id") int mate_id) {
//        ApiResponse<List> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(subMaterialService.filterEmployeematerialsByMaterialType(mate_id));
//        return apiResponse;
//    }
}
