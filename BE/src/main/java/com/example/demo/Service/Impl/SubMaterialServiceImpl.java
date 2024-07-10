package com.example.demo.Service.Impl;

//import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.ProductDTO.QuantityTotalDTO;
import com.example.demo.Dto.SubMaterialDTO.*;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.SubMaterialService;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Autowired
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private RequestProductRepository requestProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private Employee_Material_Repository employeeMaterialRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public List<SubMaterialViewDTO> getAll() {
        return subMaterialsRepository.getAllSubmaterial();
    }

    @Override
    public List<SubMaterialViewDTO> FilterByMaterial(int material_id) {
        List<SubMaterialViewDTO> subMaterialsList = subMaterialsRepository.findSubMaterialIdByMaterial(material_id);
        if(subMaterialsList.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return subMaterialsList;
    }

    @Override
    public SubMaterials addNew(SubMaterialDTO subMaterialDTO) {
        SubMaterials subMaterials = new SubMaterials();
        subMaterials.setSubMaterialName(subMaterialDTO.getSub_material_name());
        Materials materials = materialRepository.findByName(subMaterialDTO.getMaterial_name());
        subMaterials.setMaterial(materials);
        subMaterials.setQuantity(subMaterialDTO.getQuantity());
        subMaterials.setUnitPrice(subMaterialDTO.getUnit_price());
        subMaterials.setDescription(subMaterialDTO.getDescription());

        if (!checkConditionService.checkInputName(subMaterialDTO.getSub_material_name())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
//        if (subMaterialsRepository.countBySubMaterialName(subMaterialDTO.getSub_material_name()) > 0) {
//            throw new AppException(ErrorCode.NAME_EXIST);
//        }
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

                int countSubMaterials = subMaterialDTOs.size();
                int i = 1;
                HashMap<Integer, String> codeCount = generateMultipleCode(countSubMaterials);
                for (SubMaterialDTO dto : subMaterialDTOs) {
                    String subMaterialName = dto.getSub_material_name();
                    String materialName = dto.getMaterial_name(); // Lấy tên vật liệu từ DTO

                    SubMaterials existingSubMaterial = subMaterialsRepository.findBySubmaterialNameAndMaterialName(
                            subMaterialName, materialName); // Tìm kiếm theo cả tên và vật liệu

                    if (existingSubMaterial != null) {
                        // Nếu đã tồn tại SubMaterial với tên và vật liệu này, cập nhật số lượng
                        existingSubMaterial.setQuantity(existingSubMaterial.getQuantity() + dto.getQuantity());
                        subMaterialsList.add(existingSubMaterial); // Thêm vào danh sách để save sau
                    } else {
                        // Nếu chưa tồn tại, tạo SubMaterial mới với đầy đủ thuộc tính

                        // Thực hiện các kiểm tra điều kiện
                        if (!checkConditionService.checkInputName(dto.getSub_material_name())) {
                            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
                        }
                        if (!checkConditionService.checkInputQuantity(dto.getQuantity())) {
                            throw new AppException(ErrorCode.QUANTITY_INVALID);
                        }
                        if (!checkConditionService.checkInputPrice(dto.getUnit_price())) {
                            throw new AppException(ErrorCode.PRICE_INVALID);
                        }

                        SubMaterials subMaterials = new SubMaterials();
                        subMaterials.setSubMaterialName(subMaterialName);
                        // Lấy Material (nên kiểm tra null để tránh lỗi)
                        Materials materials = materialRepository.findByName(materialName);
                        subMaterials.setMaterial(materials);
                        subMaterials.setQuantity(dto.getQuantity());
                        subMaterials.setUnitPrice(dto.getUnit_price());
                        subMaterials.setDescription(dto.getDescription());
                        subMaterials.setCode(codeCount.get(i));
                        subMaterialsList.add(subMaterials); // Thêm vào danh sách để save sau
                        i++;
                    }
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

    @Transactional
    public HashMap<Integer, String> generateMultipleCode(int number) {
        HashMap<Integer, String> codeMap = new HashMap<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        SubMaterials lastsubMaterials = subMaterialsRepository.findSubMaterialsTop(dateString + "SMR");
        int count = lastsubMaterials != null ? Integer.parseInt(lastsubMaterials.getCode().substring(9)) : 0;
        for (int i = 0; i < number; i++) {
            count++;
            String code = dateString + "SMR" + String.format("%03d", count);
            codeMap.put(i + 1, code);
        }
        return codeMap;
    }

    @Override
    public List<SubMaterialNameDTO> GetListName() {
        return subMaterialsRepository.findAll().stream()
                .map(sub -> modelMapper.map(sub, SubMaterialNameDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public List<SubMaterialViewDTO> SearchByNameorCode(String key){
        return subMaterialsRepository.findSubMaterialsByNameCode(key);
    }
    @Transactional
    @Override
    public UpdateSubDTO UpdateSub(int id, UpdateSubDTO updateSubDTO){
        SubMaterials subMaterials = subMaterialsRepository.findById1(id);
        subMaterialsRepository.updateSubMaterials(id, updateSubDTO.getSub_material_name(),updateSubDTO.getDescription()
        , updateSubDTO.getQuantity(),updateSubDTO.getUnit_price());
        return modelMapper.map(subMaterials,UpdateSubDTO.class);
    }

    @Override
    public SubMaterialViewDTO getSubMaterialById(int sub_material_id) {
        SubMaterialViewDTO subMaterials = subMaterialsRepository.findSubMaterialsById(sub_material_id);
        if(subMaterials == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return subMaterials;
    }

    @Override
    public List<Product_SubmaterialDTO> getProductSubMaterialByProductId(int id,int material_id) {
        List<Product_SubmaterialDTO> productSubMaterialsList = productSubMaterialsRepository.getProductSubMaterialByProductIdAndTypeMate(id,material_id);
        if(productSubMaterialsList == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productSubMaterialsList;
    }

    @Override
    public List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByRequestProductId(int id,int material_id) {
        List<ReProduct_SubmaterialDTO> requestProductsSubmaterialsList = requestProductsSubmaterialsRepository.getRequestProductSubMaterialByProductIdAndTypeMate(id,material_id);
        if(requestProductsSubmaterialsList == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return requestProductsSubmaterialsList;
    }


    @Override
    public List<Employeematerials> getAllEmpMate() {
        List<Employeematerials> employeematerialsList = employeeMaterialRepository.findAll();
        if(employeematerialsList == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return employeematerialsList;
    }

    @Override
    public List<Employeematerials> findEmployeematerialsByName(String key) {
        List<Employeematerials> employeematerialsList = employeeMaterialRepository.findEmployeematerialsByName(key);
        if(employeematerialsList == null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return employeematerialsList;
    }

    @Override
    public SubMaterialViewDTO EditSubMaterial(int id, SubMaterialViewDTO subMaterialViewDTO) {
        SubMaterials subMaterials = subMaterialsRepository.findById1(id);
        subMaterials.setQuantity(subMaterialViewDTO.getQuantity());
        subMaterials.setUnitPrice(subMaterialViewDTO.getUnit_price());
        subMaterials.getMaterial().setMaterialId(subMaterialViewDTO.getMaterialId());
        subMaterials.setDescription(subMaterialViewDTO.getDescription());
        subMaterials.setSubMaterialName(subMaterialViewDTO.getSubMaterialName());
        subMaterialsRepository.save(subMaterials);
        return subMaterialsRepository.findSubMaterialsById(id);
    }

//    @Override
//    public List<Employeematerials> filterEmployeematerialsByMaterialType(int materialId) {
//        List<Employeematerials> employeematerialsList = employeeMaterialRepository.filterEmployeematerialsByMaterialType(materialId);
//        if(employeematerialsList == null){
//            throw new AppException(ErrorCode.NOT_FOUND);
//        }
//        return employeematerialsList;
//    }

    @Transactional
    @Override
    public List<ProductSubMaterials> createExportMaterialProduct(int productId, Map<Integer, Double> subMaterialQuantities) {
        Products product = productRepository.findById(productId);
        List<ProductSubMaterials> productSubMaterialsList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            int subMaterialId = entry.getKey();
            double quantity = entry.getValue();
            SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);
            ProductSubMaterials productSubMaterial = new ProductSubMaterials(subMaterial, product, quantity);
            productSubMaterialsList.add(productSubMaterial);
        }
        productSubMaterialsRepository.saveAll(productSubMaterialsList);
        return productSubMaterialsList;
    }

    @Transactional
    @Override
    public ResponseEntity<ApiResponse<List<String>>> createExportMaterialRequestTotalJob(int productId,int mate_id,  QuantityTotalDTO quantityTotalDTO,int emp_id) {
        //lấy các ProductSubMaterials tạo nên product có id kia
        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = requestProductsSubmaterialsRepository.findByRequestProductIDAndMate(productId,mate_id);
        List<Employeematerials> employeeMaterialsList = new ArrayList<>();
        User user = userRepository.findByIdCheck(emp_id);
        Map<String, String> errors = new HashMap<>(); //hashmap cho error
        //vòng lặp này để kiểm tra số lượng
        for (RequestProductsSubmaterials requestProductsSubmaterials : requestProductsSubmaterialsList) {
            //int quantity_product là số lượng product phải làm có trong 1 job --> quantity nhận được sẽ bằng quantity dự tính cảu 1 cái x quantity_product
            double quantity = quantityTotalDTO.getQuantity_product() * requestProductsSubmaterials.getQuantity();
            SubMaterials subMaterial = requestProductsSubmaterials.getSubMaterial();
            double currentQuantity = subMaterial.getQuantity();
            if (quantity > currentQuantity) {
                errors.put(subMaterial.getSubMaterialName(), "Không đủ số lượng");
                //continue;
            }
        }
        ApiResponse<List<String>> apiResponse = new ApiResponse<>();
        if (!errors.isEmpty()) {
            apiResponse.setError(1028, errors);
            return ResponseEntity.badRequest().body(apiResponse);
        } else {
            // Vòng lặp thứ hai: Cập nhật số lượng nếu tất cả đều đủ
            for (RequestProductsSubmaterials requestProductsSubmaterials : requestProductsSubmaterialsList) {
                double quantity = quantityTotalDTO.getQuantity_product() * requestProductsSubmaterials.getQuantity();
                SubMaterials subMaterial = requestProductsSubmaterials.getSubMaterial();
                double currentQuantity = subMaterial.getQuantity();
                subMaterial.setQuantity(currentQuantity - quantity);
                subMaterialsRepository.save(subMaterial);

                Employeematerials employeeMaterials = new Employeematerials();
                employeeMaterials.setRequestProductsSubmaterials(requestProductsSubmaterials);
                employeeMaterials.setEmployee(user);

            }
            apiResponse.setResult(Collections.singletonList("Xuất đơn nguyên vật liệu cho đơn hàng thành công"));
            return ResponseEntity.ok(apiResponse);
        }
    }

    @Transactional
    @Override
    public List<RequestProductsSubmaterials> createExportMaterialProductRequest(int request_product_id, Map<Integer, Double> subMaterialQuantities) {
        RequestProducts requestProducts = requestProductRepository.findById(request_product_id);
        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            int subMaterialId = entry.getKey();
            double quantity = entry.getValue();
            SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);
            RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial, requestProducts, quantity);
            requestProductsSubmaterialsList.add(requestProductsSubmaterials);
        }
        requestProductsSubmaterialsRepository.saveAll(requestProductsSubmaterialsList);
        return  requestProductsSubmaterialsList;
    }
    @Transactional
    @Override
    public ResponseEntity<ApiResponse<List<String>>> createExportMaterialProductTotalJob(int productId,int mate_id, QuantityTotalDTO quantityTotalDTO,int emp_id) {
        // int mate_id la bắt theo ví dụ thằng thựo mộc thì mate_id sẽ là mộc , tức là 1
        //lấy các ProductSubMaterials tạo nên product có id kia
        List<ProductSubMaterials> productSubMaterialsList = productSubMaterialsRepository.findByProductIDAndMate(productId,mate_id);
        List<Employeematerials> employeeMaterialsList = new ArrayList<>();
        User user = userRepository.findByIdCheck(emp_id);

        Map<String, String> errors = new HashMap<>(); //hashmap cho error
        //vòng lặp này để kiểm tra số lượng
        for (ProductSubMaterials productSubMaterials : productSubMaterialsList) {
            //int quantity_product là số lượng product phải làm có trong 1 job --> quantity nhận được sẽ bằng quantity dự tính cảu 1 cái x quantity_product
            double quantity = quantityTotalDTO.getQuantity_product() * productSubMaterials.getQuantity();
            SubMaterials subMaterial = productSubMaterials.getSubMaterial();
            double currentQuantity = subMaterial.getQuantity();
            if (quantity > currentQuantity) {
                errors.put(subMaterial.getSubMaterialName(), "Không đủ số lượng");
                //continue;
            }
        }
        ApiResponse<List<String>> apiResponse = new ApiResponse<>();
        if (!errors.isEmpty()) {
            apiResponse.setError(1028, errors);
            return ResponseEntity.badRequest().body(apiResponse);
        } else {
            // Vòng lặp thứ hai: Cập nhật số lượng nếu tất cả đều đủ
            for (ProductSubMaterials productSubMaterials : productSubMaterialsList) {
                double quantity = quantityTotalDTO.getQuantity_product() * productSubMaterials.getQuantity();
                SubMaterials subMaterial = productSubMaterials.getSubMaterial();
                double currentQuantity = subMaterial.getQuantity();
                subMaterial.setQuantity(currentQuantity - quantity);
                subMaterialsRepository.save(subMaterial);

                Employeematerials employeeMaterials = new Employeematerials();
                employeeMaterials.setProductSubMaterial(productSubMaterials);
                employeeMaterials.setEmployee(user);

                // Lưu từng đối tượng và thêm vào danh sách kết quả
                employeeMaterialsList.add(employeeMaterialRepository.save(employeeMaterials));
            }
            apiResponse.setResult(Collections.singletonList("Xuất đơn nguyên vật liệu cho đơn hàng thành công"));
            return ResponseEntity.ok(apiResponse);
        }
    }
//    @Override
//    public List<Employeematerials> createEMaterial(int emp_id, int mate_id, int product_id) {
//        List<Employeematerials> employeeMaterialsList = new ArrayList<>();
//        User user = userRepository.findByIdCheck(emp_id);
//
//        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = requestProductsSubmaterialsRepository.findByRequestProductIDAndMate(product_id, mate_id);
//        List<ProductSubMaterials> productSubMaterialsList = productSubMaterialsRepository.findByProductIDAndMate(product_id, mate_id);
//
//        // Nếu requestProductsSubmaterialsList KHÔNG trống (có dữ liệu)
//        if (!requestProductsSubmaterialsList.isEmpty()) {
//            for (RequestProductsSubmaterials requestProductsSubmaterials : requestProductsSubmaterialsList) {
//                Employeematerials employeeMaterials = new Employeematerials();
//                employeeMaterials.setRequestProductsSubmaterials(requestProductsSubmaterials);
//                employeeMaterials.setEmployee(user);
//
//                // Lưu từng đối tượng và thêm vào danh sách kết quả
//                employeeMaterialsList.add(employeeMaterialRepository.save(employeeMaterials));
//            }
//        }
//
//        // Nếu productSubMaterialsList KHÔNG trống (có dữ liệu)
//        if (!productSubMaterialsList.isEmpty()) {
//            for (ProductSubMaterials productSubMaterials : productSubMaterialsList) {
//                Employeematerials employeeMaterials = new Employeematerials();
//                employeeMaterials.setProductSubMaterial(productSubMaterials);
//                employeeMaterials.setEmployee(user);
//
//                // Lưu từng đối tượng và thêm vào danh sách kết quả
//                employeeMaterialsList.add(employeeMaterialRepository.save(employeeMaterials));
//            }
//        }
//
//        return employeeMaterialsList;
//    }




//    public Boolean checkSubMaterialName(String name) {
//        Pattern p = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$"); // Chấp nhận cả dấu tiếng Việt và khoảng trắng
//        return p.matcher(name).find();
//    }
//
//    public boolean checkInputQuantity(int number) {
//        return number > 0; // Kiểm tra trực tiếp xem số có lớn hơn 0 hay không
//    }
//
//    public boolean checkInputPrice(BigDecimal number) {
//        return number.compareTo(BigDecimal.ZERO) > 0;
//    }


    //phân nguyên vật liệu cho employee (bảng employee_material)


}