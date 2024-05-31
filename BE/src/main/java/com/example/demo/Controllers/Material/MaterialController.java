package com.example.demo.Controllers.Material;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Entity.Materials;
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
    @GetMapping("/getAll")
    public ApiResponse<?> getAllMaterials(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.getAllMaterials());
        return apiResponse;
    }

    @PostMapping("/addNewMaterial")
    public ApiResponse<?> getAllMaterials(@RequestBody MaterialDTO materialDTO){
        ApiResponse<Materials> apiResponse = new ApiResponse<>();
        apiResponse.setResult(materialService.AddNewMaterial(materialDTO));
        return apiResponse;
    }
}
