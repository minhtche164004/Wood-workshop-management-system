package com.example.demo.Service;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Entity.Materials;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService {
    List<Materials> getAllMaterials();
Materials AddNewMaterial(MaterialDTO materialDTO);
List<MaterialDTO> GetListName();
}
