package com.example.demo.Service.Impl;

import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierMaterialDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Entity.Suppliermaterial;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.SubMaterialsRepository;
import com.example.demo.Repository.SuppliermaterialRepository;
import com.example.demo.Service.SubMaterialService;
import com.example.demo.Service.SupplierMaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SupplierMaterialImpl implements SupplierMaterialService {
@Autowired
private SuppliermaterialRepository suppliermaterialRepository;
@Autowired
private SubMaterialsRepository subMaterialsRepository;
@Autowired
private ModelMapper modelMapper;

    @Override
    public List<Suppliermaterial> GetAllSupplier() {
        return suppliermaterialRepository.findAll();
    }

    @Override
    public Suppliermaterial AddNewSupplier(SupplierMaterialDTO supplierMaterialDTO) {
        Suppliermaterial suppliermaterial = new Suppliermaterial();
        suppliermaterial.setSupplierName(supplierMaterialDTO.getSupplierName());
        suppliermaterial.setPhoneNumber(supplierMaterialDTO.getPhoneNumber());
        if (!checkSupplierMaterialName(supplierMaterialDTO.getSupplierName())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (suppliermaterialRepository.countSuppliermaterialBySupplierName(supplierMaterialDTO.getSupplierName())>0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        suppliermaterial.setSubMaterial(subMaterialsRepository.findById1(supplierMaterialDTO.getSub_material_id()));

suppliermaterialRepository.save(suppliermaterial);
        return suppliermaterial;
    }

    @Override
    public List<SupplierNameDTO> GetListName() {
        return suppliermaterialRepository.findAll().stream()
                .map(supp -> modelMapper.map(supp, SupplierNameDTO.class))
                .collect(Collectors.toList());
    }

    public Boolean checkSupplierMaterialName(String name) {
        Pattern p = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$"); // Chấp nhận cả dấu tiếng Việt và khoảng trắng
        return p.matcher(name).find();
    }



}
