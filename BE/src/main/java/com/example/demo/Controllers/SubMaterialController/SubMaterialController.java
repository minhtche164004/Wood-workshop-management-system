package com.example.demo.Controllers.SubMaterialController;

import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.SubMaterialService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/submaterial/")
@AllArgsConstructor
public class SubMaterialController {
    @Autowired
    private SubMaterialService subMaterialService;
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

}
