package com.example.demo.Service.Impl;

import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierMaterialDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Entity.ProductSubMaterials;
import com.example.demo.Entity.RequestProductsSubmaterials;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Entity.Suppliermaterial;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.ProductSubMaterialsRepository;
import com.example.demo.Repository.RequestProductsSubmaterialsRepository;
import com.example.demo.Repository.SubMaterialsRepository;
import com.example.demo.Repository.SuppliermaterialRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.SubMaterialService;
import com.example.demo.Service.SupplierMaterialService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
@Autowired
private ModelMapper modelMapper;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Suppliermaterial> GetAllSupplier() {
        return suppliermaterialRepository.findAll();
    }

    @Override
    public Suppliermaterial AddNewSupplier(SupplierMaterialDTO supplierMaterialDTO) {
        Suppliermaterial suppliermaterial = new Suppliermaterial();
        suppliermaterial.setSupplierName(supplierMaterialDTO.getSupplierName());
        suppliermaterial.setPhoneNumber(supplierMaterialDTO.getPhoneNumber());
        if (!checkConditionService.checkInputName(supplierMaterialDTO.getSupplierName())) {
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
    @Transactional
    @Override
    public Suppliermaterial EditSupplier(int id, SupplierMaterialDTO supplierMaterialDTO){
        Suppliermaterial suppliermaterial = suppliermaterialRepository.findById(id);
        if(!checkConditionService.checkInputName(supplierMaterialDTO.getSupplierName())){
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (!supplierMaterialDTO.getSupplierName().equals(suppliermaterial.getSupplierName()) &&
                suppliermaterialRepository.findByName(supplierMaterialDTO.getSupplierName()) != null) {
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        suppliermaterial.setSupplierName(supplierMaterialDTO.getSupplierName());
        suppliermaterial.setPhoneNumber(supplierMaterialDTO.getPhoneNumber());
        SubMaterials subMaterials = subMaterialsRepository.findById1(supplierMaterialDTO.getSub_material_id());
        suppliermaterial.setSubMaterial(subMaterials);
        suppliermaterialRepository.save(suppliermaterial);
        entityManager.refresh(suppliermaterial);
        return suppliermaterial;
    }

    @Override
    public List<Suppliermaterial> SearchSupplierByName(String key){
        List<Suppliermaterial> suppliermaterialList = suppliermaterialRepository.SearchSupplierByName(key);
        if(suppliermaterialList.size() == 0){
            throw  new AppException(ErrorCode.NOT_FOUND);
        }
        return suppliermaterialList;
    }

    @Override
    public void DeleteSupplier(int id) {
       // xoá nhà cung cấp vật liệu thì những vật liệu có liên quan đến nhà cung cấp này cũng phải xoá
        //nếu hệ thống đang sử dụng nguyên liệu được cung cấp từ nhà cung cấp này thì ko thể xoá nhà cung cấp
        Suppliermaterial suppliermaterial = suppliermaterialRepository.findById(id);
        SubMaterials subMaterial = suppliermaterial.getSubMaterial();

        if (subMaterial == null) { // Xử lý trường hợp không tìm thấy SubMaterials
            suppliermaterialRepository.delete(suppliermaterial);
            return;
        }
        // Kiểm tra xem có request hoặc product nào sử dụng nguyên liệu phụ này không
        boolean isSubMaterialInUse = !requestProductsSubmaterialsRepository.findBySubMaterialId(subMaterial.getSubMaterialId()).isEmpty() ||
                !productSubMaterialsRepository.findBySubMaterialId(subMaterial.getSubMaterialId()).isEmpty();
        if (isSubMaterialInUse) {
            throw new AppException(ErrorCode.SUPPLIER_HAS_RELATIONSHIPS); // Không thể xóa nếu nguyên liệu phụ đang được sử dụng
        } else {
            suppliermaterialRepository.delete(suppliermaterial);
            subMaterialsRepository.delete(subMaterial); // Nếu nguyên liệu phụ không được sử dụng thì xóa
        }
    }






}
