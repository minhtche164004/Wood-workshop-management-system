package com.example.demo.Controllers.SubMaterialController;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
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

    @PostMapping("/AddNewSubMaterial")
    public ApiResponse<?> addNewSubMaterial(@RequestBody @Valid SubMaterialDTO subMaterialDTO) {
       ApiResponse<SubMaterials>  apiResponse = new ApiResponse<>();
       apiResponse.setResult(subMaterialService.addNew(subMaterialDTO));
       return apiResponse;
    }
    @GetMapping("/getAllName")
    public ApiResponse<?> getAllSubMaterialsName(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(subMaterialService.GetListName());
        return apiResponse;
    }
    @PostMapping("/upload-submaterial-data")
    public ApiResponse<?> uploadCustomersData(@RequestParam("file")MultipartFile file){
        ApiResponse<String> apiResponse = new ApiResponse<>();
       subMaterialService.saveSubMaterialToDatabase(file);
        apiResponse.setResult("Đọc file thành công , dữ liệu đã đưọc thêm vào ");
        return apiResponse;
    }

    @GetMapping("/download-form-submaterial-data-excel")
    public ResponseEntity<Resource> downloadFile() {
        try {
            // 1. Sử dụng ResourceLoader để lấy resource từ classpath
            Resource resource = resourceLoader.getResource("classpath:templates/submate.xlsx");

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


}
