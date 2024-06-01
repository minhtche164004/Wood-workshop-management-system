package com.example.demo.Controllers.SupplierMaterialController;


import com.example.demo.Dto.SupplierDTO.SupplierMaterialDTO;
import com.example.demo.Entity.Suppliermaterial;
import com.example.demo.Repository.SuppliermaterialRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.SupplierMaterialService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/supplier/")
@AllArgsConstructor
public class SupplierMaterialController {
    @Autowired
    private SupplierMaterialService supplierMaterialService;
    @GetMapping("/GetAllSupplier")
    public ApiResponse<?> getAllSupplier(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.GetAllSupplier());
        return  apiResponse;
    }
    @PostMapping("/AddNewSupplier")
    public ApiResponse<?> AddNewSuppler(@RequestBody @Valid SupplierMaterialDTO supplierMaterialDTO){
        ApiResponse<Suppliermaterial> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.AddNewSupplier(supplierMaterialDTO));
        return  apiResponse;
    }
    @GetMapping("/getAllName")
    public ApiResponse<?> getAllSupplierMaterialsName(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.GetListName());
        return apiResponse;
    }
}
