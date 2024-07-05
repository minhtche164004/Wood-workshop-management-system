package com.example.demo.Controllers.Material;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Entity.Suppliermaterial;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.MaterialService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRepository materialRepository;
    @GetMapping("/getAll")
    public ApiResponse<?> getAllMaterials(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.getAllMaterials());
        return apiResponse;
    }
    @GetMapping("/GetMaterialById")
    public ApiResponse<?> GetSuppliGetMaterialByIderById(@RequestParam("id") int id){
        ApiResponse<Materials> apiResponse= new ApiResponse<>();
        apiResponse.setResult(materialService.GetMaterialById(id));
        return  apiResponse;
    }

    @PostMapping("/addNewMaterial")
    public ApiResponse<?> getAllMaterials(@RequestBody MaterialDTO materialDTO){
        ApiResponse<Materials> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.AddNewMaterial(materialDTO));
        return apiResponse;
    }
    @GetMapping("/getAllName")
    public ApiResponse<?> getAllMaterialsName(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.GetListName());
        return apiResponse;
    }

    @PutMapping("/EditMaterial")
    public ApiResponse<?> EditMaterial(@RequestParam("id") int material_id,@RequestBody MaterialDTO materialDTO){
        ApiResponse<Materials> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.EditMaterial(material_id,materialDTO));
        return apiResponse;
    }
}
