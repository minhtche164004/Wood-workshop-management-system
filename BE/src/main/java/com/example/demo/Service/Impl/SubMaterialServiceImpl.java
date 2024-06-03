package com.example.demo.Service.Impl;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Repository.SubMaterialsRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.SubMaterialService;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class SubMaterialServiceImpl implements SubMaterialService {
@Autowired
private SubMaterialsRepository subMaterialsRepository;
@Autowired
private ModelMapper modelMapper;

    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private CheckConditionService checkConditionService;
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

        if (!checkConditionService.checkInputName(subMaterialDTO.getSub_material_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (subMaterialsRepository.countBySubMaterialName(subMaterialDTO.getSub_material_name())>0) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        if (!checkConditionService.checkInputQuantity(subMaterialDTO.getQuantity())) {
            throw new AppException(ErrorCode.QUANTITY_INVALID);
        }
        if (!checkConditionService.checkInputPrice(subMaterialDTO.getUnit_price())) {
            throw new AppException(ErrorCode.PRICE_INVALID);
        }

        subMaterials.setCode(generateCode());
        subMaterialsRepository.save(subMaterials);
        return subMaterials;
    }
    @Transactional
    public void saveSubMaterialToDatabase(MultipartFile file) {
        if (ExcelUploadService.isValidExcelFile(file)) {
            try {
                List<SubMaterialDTO> subMaterialDTOs = ExcelUploadService.getSubMaterialDataFromExcel(file.getInputStream());
                List<SubMaterials> subMaterialsList = new ArrayList<>();

                for (SubMaterialDTO dto : subMaterialDTOs) {
                    if (!checkConditionService.checkInputName(dto.getSub_material_name())) {
                        throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
                    }
                    if (subMaterialsRepository.countBySubMaterialName(dto.getSub_material_name()) > 0) {
                        throw new AppException(ErrorCode.NAME_EXIST);
                    }
                    if (!checkConditionService.checkInputQuantity(dto.getQuantity())) {
                        throw new AppException(ErrorCode.QUANTITY_INVALID);
                    }
                    if (!checkConditionService.checkInputPrice(dto.getUnit_price())) {
                        throw new AppException(ErrorCode.PRICE_INVALID);
                    }
                    SubMaterials subMaterials = new SubMaterials();
                    subMaterials.setSubMaterialName(dto.getSub_material_name());
                    Materials materials = materialRepository.findById1(dto.getMaterial_id());
                    subMaterials.setMaterials(materials);
                    subMaterials.setQuantity(dto.getQuantity());
                    subMaterials.setUnitPrice(dto.getUnit_price());
                    subMaterials.setDescription(dto.getDescription());



                    subMaterials.setCode(generateCode());
                    subMaterialsList.add(subMaterials);
                }

                subMaterialsRepository.saveAll(subMaterialsList);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_EXCEL_INVALID);
            }
        }
    }
    @Transactional
    public String generateCode() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        SubMaterials lastsubMaterials = subMaterialsRepository.findSubMaterialsTop(dateString + "SMR");
        int count = lastsubMaterials != null ? Integer.parseInt(lastsubMaterials.getCode().substring(9)) + 1 : 1;
        String code = dateString + "SMR" + String.format("%03d", count);
      return code;
    }

    @Override
    public List<SubMaterialNameDTO> GetListName() {
        return subMaterialsRepository.findAll().stream()
                .map(sub -> modelMapper.map(sub, SubMaterialNameDTO.class))
                .collect(Collectors.toList());
    }



}
