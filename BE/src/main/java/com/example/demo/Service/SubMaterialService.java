package com.example.demo.Service;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Entity.SubMaterials;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SubMaterialService {
    List<SubMaterials> getAll();
    SubMaterials addNew(SubMaterialDTO subMaterialDTO);
    List<SubMaterialNameDTO> GetListName();
}
