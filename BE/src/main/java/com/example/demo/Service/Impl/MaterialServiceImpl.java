package com.example.demo.Service.Impl;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.MaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl implements MaterialService {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Materials> getAllMaterials() {
        return materialRepository.findAll();
    }

    @Override
    public Materials AddNewMaterial(MaterialDTO materialDTO) {
        Materials materials = new Materials(
             materialDTO.getMaterialName()
        );
        if (!checkConditionService.checkInputName(materialDTO.getMaterialName())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (materialRepository.countByMaterialName(materialDTO.getMaterialName())>0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        materialRepository.save(materials);
        return materials;
    }

    @Override
    public List<MaterialDTO> GetListName() {
        return materialRepository.findAll().stream()
                .map(material -> modelMapper.map(material, MaterialDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Materials EditMaterial(int id,MaterialDTO materialDTO){
        materialRepository.updateMaterial(id, materialDTO.getMaterialName());
       return materialRepository.findById1(id);
    }
//    @Override
//    public void DeleteMaterial(int id){
//        materialRepository.deleteById(id);
//    }




}
