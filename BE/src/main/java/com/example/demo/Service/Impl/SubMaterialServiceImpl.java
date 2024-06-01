package com.example.demo.Service.Impl;

import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Repository.SubMaterialsRepository;
import com.example.demo.Service.SubMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SubMaterialServiceImpl implements SubMaterialService {
@Autowired
private SubMaterialsRepository subMaterialsRepository;

    @Autowired
    private MaterialRepository materialRepository;
    @Override
    public List<SubMaterials> getAll() {
        return subMaterialsRepository.findAll();
    }

    @Override
    public SubMaterials addNew(SubMaterialDTO subMaterialDTO) {
        SubMaterials subMaterials = new SubMaterials();
        subMaterials.setSubMaterialName(subMaterialDTO.getSub_material_name());
        Materials materials = materialRepository.findById1(subMaterialDTO.getMaterial_id());
        subMaterials.setMaterials(materials);
        subMaterials.setQuantity(subMaterialDTO.getQuantity());
        subMaterials.setUnitPrice(subMaterialDTO.getUnit_price());
        subMaterials.setDescription(subMaterialDTO.getDescription());

        if (!checkSubMaterialName(subMaterialDTO.getSub_material_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (subMaterialsRepository.countBySubMaterialName(subMaterialDTO.getSub_material_name())>0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);

        SubMaterials lastsubMaterials = subMaterialsRepository.findSubMaterialsTop(dateString + "SMR");
        int count = lastsubMaterials != null ? Integer.parseInt(lastsubMaterials.getCode().substring(9)) + 1 : 1;
        String code = dateString + "SMR" + String.format("%03d", count);

        subMaterials.setCode(code);
        subMaterialsRepository.save(subMaterials);
        return subMaterials;
    }

    public Boolean checkSubMaterialName(String name) {
        Pattern p = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$"); // Chấp nhận cả dấu tiếng Việt và khoảng trắng
        return p.matcher(name).find();
    }
}
