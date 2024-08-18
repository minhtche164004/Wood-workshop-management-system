package com.example.demo.Service.Impl;

//import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.JobDTO.Employee_MaterialDTO;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.ProductDTO.CreateExportMaterialProductRequest;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private RequestProductRepository requestProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;
    @Autowired
    private ProductSubMaterialsRepository productSubMaterialsRepository;
    @Autowired
    private Employee_Material_Repository employeeMaterialRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InputSubMaterialRepository inputSubMaterialRepository;
    @Autowired
    private SharedData sharedData;
    @Autowired
    private ShareDataRequest sharedDataRequest;


    LocalDate today = LocalDate.now();
    Date create = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
//    LocalDateTime today_now =  LocalDateTime.now();

    @Override
    public InputSubMaterial getLastBySubMaterialId(int sub_id) {
        return inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialId(sub_id);
    }

    @Override
    public List<SubMaterialViewDTO> getAll() {
        return subMaterialsRepository.getAllSubmaterial();
    }

    @Override
    public List<SubMaterialViewDTO> FilterByMaterial(int material_id) {
        List<SubMaterialViewDTO> subMaterialsList = subMaterialsRepository.findSubMaterialIdByMaterial(material_id);
        if (subMaterialsList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return subMaterialsList;
    }

    @Transactional
    @Override
    public SubMaterials addNew(SubMaterialDTO subMaterialDTO) { //nhập nguyên vật liệu (thì có giá nhập , còn giá bán chưa có )


        SubMaterials subMaterials = new SubMaterials();
        subMaterials.setSubMaterialName(subMaterialDTO.getSub_material_name().trim());
        Materials materials = materialRepository.findByName(subMaterialDTO.getMaterial_name().trim());
        subMaterials.setMaterial(materials);
        //   subMaterials.setCreate_date(create);
        //  subMaterials.setQuantity(subMaterialDTO.getQuantity());
        //  subMaterials.setUnitPrice(subMaterialDTO.getUnit_price());//mới đầu nhập nguyên vật liệu thì giá bán là 0
        //  subMaterials.setInputPrice(subMaterialDTO.getInput_price());

        subMaterials.setDescription(subMaterialDTO.getDescription().trim());

//        if (!checkConditionService.checkInputName(subMaterialDTO.getSub_material_name())) {
//            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
//        }
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
        //                    SubMaterials existingSubMaterial = subMaterialsRepository.findBySubmaterialNameAndMaterialName(subMaterialName, materialName);
//
//                    if (existingSubMaterial != null) {
        //thêm submaterial và bảng InputSubMaterial để lưu lại giá
        InputSubMaterial input = new InputSubMaterial();
        Action_Type actionType = subMaterialsRepository.findByIdAction(2);//tức là kiểu nhập kho
        input.setActionType(actionType);
        input.setSubMaterials(subMaterials);
        input.setInput_price(subMaterialDTO.getInput_price());
        input.setOut_price(subMaterialDTO.getUnit_price()); //lưu vào lịch sử giá nhập , còn giá bán chưa có thì cho null
//        input.setTotal_quantity(subMaterialDTO.getQuantity());
        input.setDate_input(create);//ngày nhập vào hệ thống
        input.setCreate_date(subMaterialDTO.getDate_ware_house()); //ngày nhập kho
        input.setQuantity(subMaterialDTO.getQuantity());//số lượng trong kho
        String code = generateCodeInputSubMaterial();
        input.setCode_input(code);//mã xuất nhập kho
        inputSubMaterialRepository.save(input);
        return subMaterials;
    }
    @Transactional
    public String generateCodeInputSubMaterial() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        String dateString = today.format(formatter);
        InputSubMaterial lastsubMaterials = inputSubMaterialRepository.findInputSubMaterialTop(dateString + "IPM");
        int count = lastsubMaterials != null ? Integer.parseInt(lastsubMaterials.getCode_input().substring(9)) + 1 : 1;
        String code = dateString + "IPM" + String.format("%03d", count);
        return code;
    }

    @Transactional
    public List<ExcelError> saveSubMaterialToDatabase(MultipartFile file) {
        LocalDate today = LocalDate.now();
        Date create = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
        List<ExcelError> errors = new ArrayList<>(); // Khởi tạo danh sách lỗi

        if (ExcelUploadService.isValidExcelFile(file)) {
            try {
                List<SubMaterialDTO> subMaterialDTOs = ExcelUploadService.getSubMaterialDataFromExcel(file.getInputStream(), errors);
                List<SubMaterials> subMaterialsList = new ArrayList<>();

                int countSubMaterials = subMaterialDTOs.size();
                int i = 1;
                HashMap<Integer, String> codeCount = generateMultipleCode(countSubMaterials);
                int rowIndex = 1; // Bắt đầu từ hàng 1 (vì hàng 0 là tiêu đề)
                for (SubMaterialDTO dto : subMaterialDTOs) {
                    rowIndex++; // Tăng số hàng trước khi xử lý từng dòng


                    // Thực hiện các kiểm tra điều kiện
                    if (!checkConditionService.checkInputName(dto.getSub_material_name())) {
                        errors.add(new ExcelError(rowIndex, 1, "Tên không hợp lệ"));
                    }
                    if (!checkConditionService.checkInputQuantity(dto.getQuantity())) {
                        errors.add(new ExcelError(rowIndex, 4, "Số lượng không hợp lệ"));
                    }
                    if (!checkConditionService.checkInputPrice(dto.getUnit_price())) {
                        errors.add(new ExcelError(rowIndex, 5, "Đơn giá không hợp lệ"));
                    }
                    if (!checkConditionService.checkInputPrice(dto.getInput_price())) {
                        errors.add(new ExcelError(rowIndex, 5, "Đơn giá không hợp lệ"));
                    }

                    String subMaterialName = dto.getSub_material_name().trim();
                    String materialName = dto.getMaterial_name().trim(); // Lấy tên vật liệu từ DTO
                    BigDecimal unit_price = dto.getUnit_price();

                    if(errors.isEmpty()){
                        SubMaterials subMaterials = new SubMaterials();
                        subMaterials.setSubMaterialName(subMaterialName);
                        // Lấy Material (nên kiểm tra null để tránh lỗi)
                        Materials materials = materialRepository.findByName(materialName);
                        //    subMaterials.setCreate_date(create);
                        subMaterials.setMaterial(materials);
                        //    subMaterials.setQuantity(dto.getQuantity());
                        //    subMaterials.setUnitPrice(dto.getUnit_price());
                        //    subMaterials.setInputPrice(dto.getInput_price());
                        subMaterials.setDescription(dto.getDescription().trim());
                        subMaterials.setCode(codeCount.get(i));
                        subMaterialsList.add(subMaterials); // Thêm vào danh sách để save sau
                        subMaterialsRepository.save(subMaterials);
                        InputSubMaterial input = new InputSubMaterial();
                        Action_Type actionType = subMaterialsRepository.findByIdAction(2);//tức là kiểu nhập kho
                        input.setActionType(actionType);
                        input.setSubMaterials(subMaterials);
                        input.setOut_price(dto.getUnit_price());
                        input.setInput_price(dto.getInput_price()); //lưu vào lịch sử giá nhập , còn giá bán chưa có thì cho null
                        input.setQuantity(dto.getQuantity());
                        String code = generateCodeInputSubMaterial();
                        input.setCode_input(code);
                        input.setDate_input(create);//ngày nhập vào hệ thống
                        input.setCreate_date(dto.getDate_ware_house());//ngày nhập kho
                        inputSubMaterialRepository.save(input);
                    }
                    i++;
                }
             //   subMaterialsRepository.saveAll(subMaterialsList);
                //   }


            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_EXCEL_INVALID);
            }
        }
        return errors; // Trả về danh sách lỗi
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
    public List<SubMaterialViewDTO> SearchByNameorCode(String key) {
        return subMaterialsRepository.findSubMaterialsByNameCode(key);
    }

//    @Transactional
//    @Override
//    public UpdateSubDTO UpdateSub(int id, UpdateSubDTO updateSubDTO) {
//        SubMaterials subMaterials = subMaterialsRepository.findById1(id);
//        subMaterialsRepository.updateSubMaterials(id, updateSubDTO.getSub_material_name(), updateSubDTO.getDescription()
//                , updateSubDTO.getQuantity(), updateSubDTO.getUnit_price());
//        //thêm submaterial và bảng InputSubMaterial để lưu lại giá
//        InputSubMaterial input = new InputSubMaterial();
//        input.setSubMaterials(subMaterials);
//        input.setUnit_price(updateSubDTO.getUnit_price());
//        input.setQuantity(updateSubDTO.getQuantity());
//        input.setDate_input(create);
//        inputSubMaterialRepository.save(input);
//        return modelMapper.map(subMaterials, UpdateSubDTO.class);
//    }

    @Override
    public SubMaterialViewDTO getSubMaterialById(int input_id) {
        SubMaterialViewDTO subMaterials = subMaterialsRepository.findSubMaterialsById(input_id);
        if (subMaterials == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return subMaterials;
    }

    @Override
    public List<SubMaterialViewDTO> MultiFilterSubmaterial(String search, Integer materialId) {
        List<SubMaterialViewDTO> inputSubMaterials = new ArrayList<>();

        if (search != null || materialId != null ) {
            inputSubMaterials = subMaterialsRepository.MultiFilterSubmaterial(search, materialId);
        } else {
            inputSubMaterials =subMaterialsRepository.getAllSubmaterial();
        }

        if (inputSubMaterials.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return inputSubMaterials;
    }

    @Override
    public List<Product_SubmaterialDTO> getProductSubMaterialByProductId(int id, int material_id) {
        List<Product_SubmaterialDTO> productSubMaterialsList = productSubMaterialsRepository.getProductSubMaterialByProductIdAndTypeMate(id, material_id);
        if (productSubMaterialsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return productSubMaterialsList;
    }

    @Override
    public List<ReProduct_SubmaterialDTO> getRequestProductSubMaterialByRequestProductId(int id, int material_id) {
        List<ReProduct_SubmaterialDTO> requestProductsSubmaterialsList = requestProductsSubmaterialsRepository.getRequestProductSubMaterialByProductIdAndTypeMate(id, material_id);
        if (requestProductsSubmaterialsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return requestProductsSubmaterialsList;
    }


    @Override
    public List<Employee_MaterialDTO> getAllEmpMate() {
        List<Employee_MaterialDTO> employeematerialsList = employeeMaterialRepository.getAllEmployeeMate();
        if (employeematerialsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return employeematerialsList;
    }

    @Override
    public List<Employee_MaterialDTO> findEmployeematerialsByName(String key) {
        List<Employee_MaterialDTO> employeematerialsList = employeeMaterialRepository.getAllEmployeeMateByNameEmployee(key);
        if (employeematerialsList == null) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return employeematerialsList;
    }

    //lúc edit giá thì taọ 1 bản ghi mới trong bảng input , nhưng mã xuất nhập kho vẫn là của cái cũ


    @Override
    public SubMaterialViewDTO EditSubMaterial(int id, SubMaterialViewDTO subMaterialViewDTO) {
        // Kiểm tra xem có thay đổi gì không
        boolean isPriceUpdated = false;
        boolean isQuantityUpdated=false;
//        boolean isSubQuantity = false;
        InputSubMaterial input_sub_last = inputSubMaterialRepository.findById(id);
        //    SubMaterials subMaterials = subMaterialsRepository.findById1(id);
        SubMaterials subMaterials = subMaterialsRepository.findById1(input_sub_last.getSubMaterials().getSubMaterialId());
        //   InputSubMaterial input_sub_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialId(id);
        if(input_sub_last.getOut_price().compareTo(subMaterialViewDTO.getUnitPrice()) != 0 && subMaterialViewDTO.getQuantity() > 0){ //đây là cập nhật giá bán
            isPriceUpdated = true;
        }

        double epsilon = 0.00001; // Hoặc một giá trị epsilon phù hợp
        if (Math.abs(input_sub_last.getQuantity() - subMaterialViewDTO.getQuantity()) > epsilon && subMaterialViewDTO.getQuantity() > 0) {
            isQuantityUpdated = true;
        }
        //quantity < 0 nghĩa là nếu lô hàng ấy có lỗi hay gì đấy thì trừ đi số lượng trong kho đấy

        if(subMaterialViewDTO.getQuantity() < 0){
            if(input_sub_last.getQuantity() < -1*subMaterialViewDTO.getQuantity()){
                throw new AppException(ErrorCode.QUANTITY_INPUT_EXPORT);
            }
            InputSubMaterial input = new InputSubMaterial();
            input.setCode_input(input_sub_last.getCode_input());
            input.setQuantity(input_sub_last.getQuantity() + subMaterialViewDTO.getQuantity());
            input.setSubMaterials(input_sub_last.getSubMaterials());
            input.setInput_price(input_sub_last.getInput_price());
            input.setOut_price(input_sub_last.getOut_price());
            input.setCreate_date(input_sub_last.getCreate_date());
            input.setDate_input(create);
            input.setReason_export(subMaterialViewDTO.getReason_export());
            input.setActionType(subMaterialsRepository.findByIdAction(1));//xuất kho
            inputSubMaterialRepository.save(input);

           // input_sub_last.setQuantity(input_sub_last.getQuantity() + subMaterialViewDTO.getQuantity());
           // inputSubMaterialRepository.save(input_sub_last);
        }
        if (isPriceUpdated == true || isQuantityUpdated == true) {
            // Chỉ lưu vào bảng InputSubMaterial khi có thay đổi
            InputSubMaterial input = new InputSubMaterial();
            String code = generateCodeInputSubMaterial();
            input.setSubMaterials(subMaterials);
            input.setInput_price(input_sub_last.getInput_price()); //lưu vào lịch sử giá nhập

            if (isPriceUpdated) {
                input.setCode_input(input_sub_last.getCode_input());//mã xuất nhập kho thì vẫn cảu thằng cũ
                input.setOut_price(subMaterialViewDTO.getUnitPrice()); // Giá bán mới
            } else {
                input.setOut_price(input_sub_last.getOut_price()); // Giữ nguyên giá bán cũ
            }

            if (isQuantityUpdated) {
                input.setCode_input(code);
                input.setQuantity(input_sub_last.getQuantity()+subMaterialViewDTO.getQuantity() - input_sub_last.getQuantity()); // set số lượng thêm vào hoặc trừ đi
//                input.setChange_quantity(subMaterialViewDTO.getQuantity() - input_sub_last.getQuantity());
            } else {
                input.setQuantity(input_sub_last.getQuantity()); // Giữ nguyên số lượng cũ
//                input.setChange_quantity(0);
            }
            if (isPriceUpdated && isQuantityUpdated) {
                input.setCode_input(code);
            }

            input.setDate_input(create);


            input.setActionType(subMaterialsRepository.findByIdAction(2));
            input.setCreate_date(input_sub_last.getCreate_date()); //ngày nhập kho
            inputSubMaterialRepository.save(input);
        }
        //    subMaterials.setQuantity(subMaterialViewDTO.getQuantity());
        //    subMaterials.setUnitPrice(subMaterialViewDTO.getUnitPrice());
        subMaterials.getMaterial().setMaterialId(subMaterialViewDTO.getMaterialId());
        subMaterials.setDescription(subMaterialViewDTO.getDescription().trim());
        subMaterials.setSubMaterialName(subMaterialViewDTO.getSubMaterialName().trim());
        updatePriceAllProduct(subMaterials.getSubMaterialId(), subMaterialViewDTO.getUnitPrice(),id);
        subMaterialsRepository.save(subMaterials);

        return subMaterialsRepository.findSubMaterialsById(id);
    }

    //@Override
    private void updatePriceAllProduct(int subMaterialId,BigDecimal unit_price,int input_id) {
        List<Products> list_product = productSubMaterialsRepository.getProductIdsBySubMaterialId(subMaterialId,input_id);
        BigDecimal current_unit_price = subMaterialsRepository.findSubMaterialsById(input_id).getUnitPrice();
        BigDecimal new_unit_price = unit_price;
        for(Products p : list_product){
            double quantity = productSubMaterialsRepository.getQuantityInProductSubMaterialsByProductId(p.getProductId(),subMaterialId);
            BigDecimal change = (new_unit_price == null ? BigDecimal.ZERO : new_unit_price)
                    .subtract(current_unit_price == null ? BigDecimal.ZERO : current_unit_price)
                    .multiply(BigDecimal.valueOf(quantity));
            if (change.compareTo(BigDecimal.ZERO) >= 0) {
                p.setPrice(p.getPrice().add(change));
            }
            productRepository.save(p);
        }
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
    public ResponseEntity<ApiResponse<List<String>>> createExportMaterialRequestTotalJob(int productId, int mate_id, QuantityTotalDTO quantityTotalDTO, int emp_id) {
        //lấy các ProductSubMaterials tạo nên product có id kia
        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = requestProductsSubmaterialsRepository.findByRequestProductIDAndMate(productId, mate_id);
        List<Employeematerials> employeeMaterialsList = new ArrayList<>();
        User user = userRepository.findByIdCheck(emp_id);
        Map<String, String> errors = new HashMap<>(); //hashmap cho error
        //vòng lặp này để kiểm tra số lượng
        for (RequestProductsSubmaterials requestProductsSubmaterials : requestProductsSubmaterialsList) {
            //int quantity_product là số lượng product phải làm có trong 1 job --> quantity nhận được sẽ bằng quantity dự tính cảu 1 cái x quantity_product
            double quantity = quantityTotalDTO.getQuantity_product() * requestProductsSubmaterials.getQuantity();
            SubMaterials subMaterial = subMaterialsRepository.findById1(requestProductsSubmaterials.getSubMaterial());
            String code_input =   requestProductsSubmaterials.getInputSubMaterial().getCode_input();
            InputSubMaterial input_sub_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input,subMaterial.getSubMaterialId());
            double currentQuantity = input_sub_last.getQuantity();
            if (quantity > currentQuantity) {
                double a = quantity - currentQuantity;
                String errorMessage = String.format("Không đủ số lượng, thiếu %.2f %s", a, subMaterial.getMaterial().getType());
                errors.put(subMaterial.getSubMaterialName(), errorMessage);
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
                SubMaterials subMaterial = subMaterialsRepository.findById1(requestProductsSubmaterials.getSubMaterial());

                String code_input =   requestProductsSubmaterials.getInputSubMaterial().getCode_input();
                InputSubMaterial input_sub_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input,subMaterial.getSubMaterialId());
                double currentQuantity = input_sub_last.getQuantity();
                //    double currentQuantity = subMaterial.getQuantity();
                //  input_sub_last.setQuantity(currentQuantity - quantity);
                inputSubMaterialRepository.save(input_sub_last);
                subMaterialsRepository.save(subMaterial);

                InputSubMaterial input = new InputSubMaterial();
                Action_Type actionType = subMaterialsRepository.findByIdAction(1);//tức là kiểu xuất kho(lưu lại lịch sử xuất kho)
                input.setActionType(actionType);
                input.setSubMaterials(subMaterial);
                input.setInput_price(input_sub_last.getInput_price());
                input.setOut_price(input_sub_last.getOut_price());
                input.setQuantity(currentQuantity - quantity);
                input.setCode_input(input_sub_last.getCode_input());
                input.setCreate_date(input_sub_last.getCreate_date());//ngày nhập kho
                input.setDate_input(create);//ngày tác động
                input.setReason_export("Xuất kho cho công việc");//lí do xuất kho
                inputSubMaterialRepository.save(input);

                Employeematerials employeeMaterials = new Employeematerials();
                employeeMaterials.setRequestProductsSubmaterials(requestProductsSubmaterials);
                employeeMaterials.setEmployee(user);
                employeeMaterials.setTotal_material(quantity);


                String code_input_sub = requestProductsSubmaterials.getInputSubMaterial().getCode_input();
                int sub_mate_id = requestProductsSubmaterials.getSubMaterial();
                InputSubMaterial input_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input_sub,sub_mate_id);
                requestProductsSubmaterials.setInputSubMaterial(input_last);
                requestProductsSubmaterialsRepository.save(requestProductsSubmaterials);
                // Lưu từng đối tượng và thêm vào danh sách kết quả
                employeeMaterialsList.add(employeeMaterialRepository.save(employeeMaterials));

            }
            sharedDataRequest.setEmployeeMaterialsList(employeeMaterialsList); // Lưu vào shared bean
            apiResponse.setResult(Collections.singletonList("Xuất đơn nguyên vật liệu cho đơn hàng thành công"));
            return ResponseEntity.ok(apiResponse);
        }
    }

    @Transactional
    @Override
    public List<ProductSubMaterials> createExportMaterialProduct(int productId, Map<Integer, Double> subMaterialQuantities) {
        Products product = productRepository.findById(productId);
        List<ProductSubMaterials> productSubMaterialsList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            int input_id = entry.getKey();
            InputSubMaterial input=inputSubMaterialRepository.findById(input_id);
            SubMaterials subMaterial = input.getSubMaterials();
            double quantity = entry.getValue();
            //  SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);

            //    InputSubMaterial input = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialId(subMaterialId);//lấy giá mới cập nhật
            ProductSubMaterials productSubMaterial = new ProductSubMaterials(subMaterial.getSubMaterialId(), product, quantity, input);
            productSubMaterialsList.add(productSubMaterial);
        }
        productSubMaterialsRepository.saveAll(productSubMaterialsList);
        return productSubMaterialsList;
    }

    @Override
    @Transactional
    public List<RequestProductsSubmaterials> createExportMaterialProductRequest(int productId, Map<Integer, Double> subMaterialQuantities) {
        RequestProducts requestProducts = requestProductRepository.findById(productId);
        List<RequestProductsSubmaterials> reproductSubMaterialsList = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
            //    int subMaterialId = entry.getKey();
            double quantity = entry.getValue();
            int input_id = entry.getKey();
            InputSubMaterial input=inputSubMaterialRepository.findById(input_id);
            SubMaterials subMaterial = input.getSubMaterials();
            //    InputSubMaterial input = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialId(subMaterialId);//lấy giá mới cập nhật
            RequestProductsSubmaterials productSubMaterial = new RequestProductsSubmaterials(subMaterial.getSubMaterialId(), requestProducts, quantity, input);
            reproductSubMaterialsList.add(productSubMaterial);
        }
        requestProductsSubmaterialsRepository.saveAll(reproductSubMaterialsList);
        return reproductSubMaterialsList;
    }

    @Override
    @Transactional
    public List<RequestProductsSubmaterials> createExportMaterialListProductRequest(List<CreateExportMaterialProductRequest> exportMaterialDTOs) {
        List<RequestProductsSubmaterials> result = new ArrayList<>();
        for (CreateExportMaterialProductRequest dto : exportMaterialDTOs) {
            int id = dto.getProductId();
            RequestProducts requestProducts = requestProductRepository.findByIdInteger(id);

            for (Map.Entry<Integer, Double> entry : dto.getSubMaterialQuantities().entrySet()) {
                int subMaterialId = entry.getKey();
                double quantity = entry.getValue();
                int input_id = entry.getKey();
                InputSubMaterial input=inputSubMaterialRepository.findById(input_id);
                SubMaterials subMaterial = input.getSubMaterials();
//                SubMaterials subMaterial = subMaterialsRepository.findById1(subMaterialId);
//                InputSubMaterial input=inputSubMaterialRepository.findById(input_id);
                //    InputSubMaterial input = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialId(subMaterialId);
                RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial.getSubMaterialId(), requestProducts, quantity, input);

                result.add(requestProductsSubmaterials);
            }
        }
        requestProductsSubmaterialsRepository.saveAll(result);
        return result;
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

            SubMaterials subMaterial =  subMaterialsRepository.findById1(productSubMaterials.getSubMaterial());
            String code_input =   productSubMaterials.getInputSubMaterial().getCode_input();
            InputSubMaterial input_sub_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input,subMaterial.getSubMaterialId());
            double currentQuantity = input_sub_last.getQuantity();
            //    double currentQuantity = subMaterial.getQuantity();
            if (quantity > currentQuantity) {
                double a = quantity-currentQuantity;
                String errorMessage = String.format("Không đủ số lượng, thiếu %.2f %s", a, subMaterial.getMaterial().getType());
                errors.put(subMaterial.getSubMaterialName(), errorMessage);
                //   errors.put(subMaterial.getSubMaterialName(), "Không đủ số lượng");
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
                SubMaterials subMaterial =  subMaterialsRepository.findById1(productSubMaterials.getSubMaterial());

                String code_input =   productSubMaterials.getInputSubMaterial().getCode_input();
                InputSubMaterial input_sub_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input,subMaterial.getSubMaterialId());
                double currentQuantity = input_sub_last.getQuantity();
                //    double currentQuantity = subMaterial.getQuantity();
                //    input_sub_last.setQuantity(currentQuantity - quantity);

                inputSubMaterialRepository.save(input_sub_last);
                subMaterialsRepository.save(subMaterial);

                InputSubMaterial input = new InputSubMaterial();
                Action_Type actionType = subMaterialsRepository.findByIdAction(1);//tức là kiểu xuất kho(lưu lại lịch sử xuất kho)
                input.setActionType(actionType);
                input.setSubMaterials(subMaterial);
                input.setInput_price(input_sub_last.getInput_price());
                input.setOut_price(input_sub_last.getOut_price());
                input.setDate_input(create);
                input.setCreate_date(input_sub_last.getCreate_date());
                input.setQuantity(currentQuantity - quantity);
                input.setReason_export("Xuất kho cho công việc");//lí do xuất kho
                //  input.setChange_quantity(quantity);
                input.setCode_input(input_sub_last.getCode_input());
                inputSubMaterialRepository.save(input);




                Employeematerials employeeMaterials = new Employeematerials();
                employeeMaterials.setProductSubMaterial(productSubMaterials);
                employeeMaterials.setEmployee(user);
                employeeMaterials.setTotal_material(quantity);
                String code_input_sub = productSubMaterials.getInputSubMaterial().getCode_input();
                int sub_mate_id = productSubMaterials.getSubMaterial();
                InputSubMaterial input_last = inputSubMaterialRepository.findLatestSubMaterialInputSubMaterialBySubMaterialIdGroupByCode(code_input_sub,sub_mate_id);
                productSubMaterials.setInputSubMaterial(input_last);
                productSubMaterialsRepository.save(productSubMaterials);
                // Lưu từng đối tượng và thêm vào danh sách kết quả
                employeeMaterialsList.add(employeeMaterialRepository.save(employeeMaterials));
            }
            sharedData.setEmployeeMaterialsList(employeeMaterialsList); // Lưu vào shared bean
            apiResponse.setResult(Collections.singletonList("Xuất đơn nguyên vật liệu cho đơn hàng thành công"));
            return ResponseEntity.ok(apiResponse);
        }
    }
    @Transactional
    @Override
    public List<ProductSubMaterials> EditSubMaterialProduct(int product_id, Map<Integer, Double> subMaterialQuantities) {
        Products products = productRepository.findById(product_id);
        List<ProductSubMaterials> list = productSubMaterialsRepository.findByProductID(product_id);
        List<ProductSubMaterials> productSubMaterialsList = new ArrayList<>();

        List<Employeematerials> list_emp = employeeMaterialRepository.findEmployeematerialsByProductId(product_id);
        if (list.isEmpty() && list_emp.isEmpty()) { //nếu không có gì trước đó
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                double quantity = entry.getValue();
                int input_id = entry.getKey();
                InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                SubMaterials subMaterial = input.getSubMaterials();
                ProductSubMaterials requestProductsSubmaterials = new ProductSubMaterials(subMaterial.getSubMaterialId(), products, quantity, input);
                productSubMaterialsList.add(requestProductsSubmaterials);
            }
            productSubMaterialsRepository.saveAll(productSubMaterialsList);
        }
        if (!list.isEmpty() && list_emp.isEmpty()) { //nếu list ước lượng đã có trước đó nhưng chưa giao nguyên liệu cho nhân viên
            List<Integer> list_id_request_sub = list.stream()
                    .map(emp -> emp.getProductSubMaterialId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
            for (int re_1 : list_id_request_sub) {
                productSubMaterialsRepository.deleteProductSubMaterialsById(re_1); // Xóa trước khi thêm mới
            }
//            for (ProductSubMaterials re_1 : list) {
//                productSubMaterialsRepository.deleteProductSubMaterialsById(re_1.getProductSubMaterialId()); // Xóa trước khi thêm mới
//            }
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                int input_id = entry.getKey();
                InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                SubMaterials subMaterial = input.getSubMaterials();
                double quantity = entry.getValue();
                ProductSubMaterials requestProductsSubmaterials = new ProductSubMaterials(subMaterial.getSubMaterialId(), products, quantity, input);
                productSubMaterialsList.add(requestProductsSubmaterials);
            }
            productSubMaterialsRepository.saveAll(productSubMaterialsList);
        }
        if (!list.isEmpty() && !list_emp.isEmpty()) { //nếu list ước lượng đã tồn tại trước dó , đồng thời có 1 số nguyên vật liệu đã và đang giao cho khách
            //kiếm những thằng nào chưa giao cho nhân viên thì xoá nó đi
            // Tạo danh sách chứa các input_id trong list_emp
            List<Integer> existingInputIdsInEmp = list_emp.stream()
                    .map(emp -> emp.getProductSubMaterial().getProductSubMaterialId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách

            // Lọc ra các requets_product_ tronid list mà không có trong list_emp
            List<Integer> inputIdsNotInEmp = list.stream()
                    .map(re -> re.getProductSubMaterialId())
                    .filter(inputId -> !existingInputIdsInEmp.contains(inputId))
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
//            System.out.println("Các input_id không tồn tại trong list_emp: " + inputIdsNotInEmp);
            List<Integer> existingInputIdsInEmp_input = list_emp.stream()
                    .map(emp -> emp.getProductSubMaterial().getInputSubMaterial().getInput_id())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách


            List<String> existingInputIdsInEmp_StringCode_Input = list_emp.stream()
                    .map(emp -> emp.getProductSubMaterial().getInputSubMaterial().getCode_input())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách

            List<Integer> existingMaterialIdInEmp_input = list_emp.stream()
                    .map(emp -> emp.getProductSubMaterial().getInputSubMaterial().getSubMaterials().getMaterial().getMaterialId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách

            List<Integer> inputIdsFromMap = subMaterialQuantities.keySet().stream()
                    .collect(Collectors.toList());
            System.out.println(inputIdsFromMap);
            List<String> code_input = new ArrayList<>();
            for(int input_id : inputIdsFromMap){
                String code =  inputSubMaterialRepository.findCodeInputById(input_id);
                code_input.add(code);
            }
            if (!code_input.containsAll(existingInputIdsInEmp_StringCode_Input)) {
                throw new AppException(ErrorCode.MATERIAL_EMPLOYEE_HAS_RELATIONSHIPS);
            }
            List<Integer> material_id = new ArrayList<>();
            for(int input_id : inputIdsFromMap){
                Integer code =  inputSubMaterialRepository.findMaterialInputById(input_id);
                material_id.add(code);
            }
            if (material_id.containsAll(existingMaterialIdInEmp_input)) {
                throw new AppException(ErrorCode.EXISTED_SUB_MATERIAL);
            }

            for (int re_1 : inputIdsNotInEmp) {
                productSubMaterialsRepository.deleteProductSubMaterialsById(re_1); // Xóa trước khi thêm mới
            }
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                int input_id = entry.getKey();
                InputSubMaterial inputSubMaterial = inputSubMaterialRepository.findById(input_id);
                if(!existingInputIdsInEmp_input.contains(input_id) && !existingInputIdsInEmp_StringCode_Input.contains(inputSubMaterial.getCode_input())) {
                    InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                    SubMaterials subMaterial = input.getSubMaterials();
                    double quantity = entry.getValue();
                    ProductSubMaterials requestProductsSubmaterials = new ProductSubMaterials(subMaterial.getSubMaterialId(), products, quantity, input);
                    productSubMaterialsList.add(requestProductsSubmaterials);
                }
            }
            productSubMaterialsRepository.saveAll(productSubMaterialsList);
        }
        return productSubMaterialsList;
    }


    @Transactional
    @Override
    public  List<RequestProductsSubmaterials> EditSubMaterialRequestProduct(int request_product_id, Map<Integer, Double> subMaterialQuantities) {
        RequestProducts requestProducts = requestProductRepository.findById(request_product_id);
        List<RequestProductsSubmaterials> list = requestProductsSubmaterialsRepository.findByRequestProductID(request_product_id);
        List<RequestProductsSubmaterials> requestProductsSubmaterialsList = new ArrayList<>();
        List<Employeematerials> list_emp = employeeMaterialRepository.findEmployeematerialsByRequestProductId(request_product_id);
        if (list.isEmpty() && list_emp.isEmpty()) { //nếu không có gì trước đó
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                double quantity = entry.getValue();
                int input_id = entry.getKey();
                InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                SubMaterials subMaterial = input.getSubMaterials();
                RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial.getSubMaterialId(), requestProducts, quantity, input);
                requestProductsSubmaterialsList.add(requestProductsSubmaterials);
            }
            requestProductsSubmaterialsRepository.saveAll(requestProductsSubmaterialsList);
        }
        if (!list.isEmpty() && list_emp.isEmpty()) { //nếu list ước lượng đã có trước đó nhưng chưa giao nguyên liệu cho nhân viên
            List<Integer> list_id_request_sub = list.stream()
                    .map(emp -> emp.getRequestProductsSubmaterialsId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
            for (int re_1 : list_id_request_sub) {
                requestProductsSubmaterialsRepository.deleteRequestProductSubMaterialsById(re_1); // Xóa trước khi thêm mới
            }

//            for (RequestProductsSubmaterials re_1 : list) {
//                requestProductsSubmaterialsRepository.deleteRequestProductSubMaterialsById(re_1.getRequestProductsSubmaterialsId()); // Xóa trước khi thêm mới
//            }
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                int input_id = entry.getKey();
                InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                SubMaterials subMaterial = input.getSubMaterials();
                double quantity = entry.getValue();
                RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial.getSubMaterialId(), requestProducts, quantity, input);
                requestProductsSubmaterialsList.add(requestProductsSubmaterials);
            }
            requestProductsSubmaterialsRepository.saveAll(requestProductsSubmaterialsList);
        }
        if (!list.isEmpty() && !list_emp.isEmpty()) { //nếu list ước lượng đã tồn tại trước dó , đồng thời có 1 số nguyên vật liệu đã và đang giao cho khách
            //kiếm những thằng nào chưa giao cho nhân viên thì xoá nó đi
            // Tạo danh sách chứa các input_id trong list_emp
            List<Integer> existingInputIdsInEmp = list_emp.stream()
                    .map(emp -> emp.getRequestProductsSubmaterials().getRequestProductsSubmaterialsId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
            // Lọc ra các requets_product_ tronid list mà không có trong list_emp
            List<Integer> inputIdsNotInEmp = list.stream()
                    .map(re -> re.getRequestProductsSubmaterialsId())
                    .filter(inputId -> !existingInputIdsInEmp.contains(inputId))
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
//            System.out.println("Các input_id không tồn tại trong list_emp: " + inputIdsNotInEmp);
            List<Integer> existingInputIdsInEmp_input = list_emp.stream()
                    .map(emp -> emp.getRequestProductsSubmaterials().getInputSubMaterial().getInput_id())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách

            List<String> existingInputIdsInEmp_StringCode_Input = list_emp.stream()
                    .map(emp -> emp.getRequestProductsSubmaterials().getInputSubMaterial().getCode_input())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách
            List<Integer> existingMaterialIdInEmp_input = list_emp.stream()
                    .map(emp -> emp.getRequestProductsSubmaterials().getInputSubMaterial().getSubMaterials().getMaterial().getMaterialId())
                    .collect(Collectors.toList());  // Sử dụng Collectors.toList() cho danh sách

///
            List<Integer> inputIdsFromMap = subMaterialQuantities.keySet().stream()
                    .collect(Collectors.toList());
            System.out.println(inputIdsFromMap);
            List<String> code_input = new ArrayList<>();
            for(int input_id : inputIdsFromMap){
                String code =  inputSubMaterialRepository.findCodeInputById(input_id);
                code_input.add(code);
            }
            if (!code_input.containsAll(existingInputIdsInEmp_StringCode_Input)) {
                throw new AppException(ErrorCode.MATERIAL_EMPLOYEE_HAS_RELATIONSHIPS);
            }
            List<Integer> material_id = new ArrayList<>();
            for(int input_id : inputIdsFromMap){
                Integer code =  inputSubMaterialRepository.findMaterialInputById(input_id);
                material_id.add(code);
            }
            if (material_id.containsAll(existingMaterialIdInEmp_input)) {
                throw new AppException(ErrorCode.EXISTED_SUB_MATERIAL);
            }

            for (int re_1 : inputIdsNotInEmp) {
                requestProductsSubmaterialsRepository.deleteRequestProductSubMaterialsById(re_1); // Xóa trước khi thêm mới
            }
            for (Map.Entry<Integer, Double> entry : subMaterialQuantities.entrySet()) {
                int input_id = entry.getKey();
                InputSubMaterial inputSubMaterial = inputSubMaterialRepository.findById(input_id);
                if(!existingInputIdsInEmp_input.contains(input_id) && !existingInputIdsInEmp_StringCode_Input.contains(inputSubMaterial.getCode_input())) {
                    InputSubMaterial input = inputSubMaterialRepository.findById(input_id);
                    SubMaterials subMaterial = input.getSubMaterials();
                    double quantity = entry.getValue();
                    RequestProductsSubmaterials requestProductsSubmaterials = new RequestProductsSubmaterials(subMaterial.getSubMaterialId(), requestProducts, quantity, input);
                    requestProductsSubmaterialsList.add(requestProductsSubmaterials);
                }
            }
            requestProductsSubmaterialsRepository.saveAll(requestProductsSubmaterialsList);
        }
        return requestProductsSubmaterialsList;
    }

    @Override
    public List<InputSubMaterial> getAllInputSubMaterial() {
        List<InputSubMaterial> list = subMaterialsRepository.getAllInputSubMaterial();
        if(list ==  null){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }

    @Override
    public List<InputSubMaterial> MultiFilterInputSubMaterial(String search, Integer materialId,Integer action_type_id, Date startDate, Date endDate, BigDecimal minPrice, BigDecimal maxPrice,String sortDirection) {
        List<InputSubMaterial> inputSubMaterials = new ArrayList<>();

        if (search != null || materialId != null || action_type_id!= null || startDate != null || endDate != null || minPrice != null || maxPrice != null) {
            inputSubMaterials = subMaterialsRepository.MultiFilterInputSubMaterial(search, materialId,action_type_id, startDate,endDate, minPrice, maxPrice);
        } else {
            inputSubMaterials = subMaterialsRepository.getAllInputSubMaterial();
        }

        if (inputSubMaterials.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        // Sắp xếp danh sách sản phẩm theo giá
        if (sortDirection != null) {
            if (sortDirection.equals("asc")) {
                inputSubMaterials.sort(Comparator.comparing(InputSubMaterial::getOut_price));
            } else if (sortDirection.equals("desc")) {
                inputSubMaterials.sort(Comparator.comparing(InputSubMaterial::getOut_price).reversed());
            }
        }

        return inputSubMaterials;
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