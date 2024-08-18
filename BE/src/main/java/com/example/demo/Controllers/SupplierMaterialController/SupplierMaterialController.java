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
    @Autowired
    private SuppliermaterialRepository suppliermaterialRepository;
    @GetMapping("/GetAllSupplier")
    public ApiResponse<?> getAllSupplier(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.GetAllSupplier());
        return  apiResponse;
    }
    @GetMapping("/GetSupplierById")
    public ApiResponse<?> GetSupplierById(@RequestParam("id") int id){
        ApiResponse<Suppliermaterial> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.GetSuppliermaterialById(id));
        return  apiResponse;
    }
    @PostMapping("/AddNewSupplier")
    public ApiResponse<?> AddNewSuppler(@RequestBody @Valid SupplierMaterialDTO supplierMaterialDTO){
        ApiResponse<Suppliermaterial> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.AddNewSupplier(supplierMaterialDTO));
        return  apiResponse;
    }

    @PutMapping("/EditSupplier")
    public ApiResponse<?> EditSupplier(@RequestBody @Valid SupplierMaterialDTO supplierMaterialDTO,@RequestParam("id") int id){
        ApiResponse<Suppliermaterial> apiResponse= new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.EditSupplier(id,supplierMaterialDTO));
        return  apiResponse;
    }
    @GetMapping("/getAllName")
    public ApiResponse<?> getAllSupplierMaterialsName(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(supplierMaterialService.GetListName());
        return apiResponse;
    }

    @GetMapping("/SearchByName")
    public ApiResponse<?> searchByName(@RequestParam("key") String key){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String searchTerm = key == null ? "" : key.trim();
        apiResponse.setResult(supplierMaterialService.SearchSupplierByName(searchTerm));
        return apiResponse;
    }

    @DeleteMapping("/DeleteSupplier")
    public ApiResponse<?> DeleteSupplier(@RequestParam("id") int id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        supplierMaterialService.DeleteSupplier(id);
        apiResponse.setResult("Xoá nhà cung cấp vật liệu thành công");
        return apiResponse;
    }
}
