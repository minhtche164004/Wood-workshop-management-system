package com.example.demo.Service;


import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierMaterialDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Entity.Suppliermaterial;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SupplierMaterialService {
    List<Suppliermaterial> GetAllSupplier();
    Suppliermaterial AddNewSupplier(SupplierMaterialDTO supplierMaterialDTO);
    List<SupplierNameDTO> GetListName();
    Suppliermaterial EditSupplier(int id,SupplierMaterialDTO supplierMaterialDTO);
    List<Suppliermaterial> SearchSupplierByName(String key);
    void DeleteSupplier(int id);
}
