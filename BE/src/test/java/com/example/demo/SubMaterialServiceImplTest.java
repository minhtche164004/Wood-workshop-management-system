package com.example.demo;

import com.example.demo.Dto.SubMaterialDTO.Product_SubmaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialViewDTO;
import com.example.demo.Entity.InputSubMaterial;
import com.example.demo.Entity.Materials;
import com.example.demo.Entity.Products;
import com.example.demo.Entity.SubMaterials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.Impl.SubMaterialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubMaterialServiceImplTest {
    @InjectMocks
    private SubMaterialServiceImpl subMaterialServiceImpl;

    @Mock
    private SubMaterialsRepository subMaterialsRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private CheckConditionService checkConditionService;

    @Mock
    private RequestProductRepository requestProductRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RequestProductsSubmaterialsRepository requestProductsSubmaterialsRepository;

    @Mock
    private ProductSubMaterialsRepository productSubMaterialsRepository;

    @Mock
    private Employee_Material_Repository employeeMaterialRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private InputSubMaterialRepository inputSubMaterialRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<SubMaterialViewDTO> mockList = Arrays.asList(new SubMaterialViewDTO(), new SubMaterialViewDTO());
        when(subMaterialsRepository.getAllSubmaterial()).thenReturn(mockList);

        List<SubMaterialViewDTO> result = subMaterialServiceImpl.getAll();

        assertEquals(2, result.size());
        verify(subMaterialsRepository).getAllSubmaterial();
    }

    @Test
    void testAddNew_ValidInput1() {
        SubMaterialDTO dto = new SubMaterialDTO();
        dto.setSub_material_name("SubMaterial1");
        dto.setMaterial_name("Material1");
        dto.setQuantity(10.0); // Use Double
        dto.setUnit_price(BigDecimal.valueOf(100));
        dto.setInput_price(BigDecimal.valueOf(50));
        dto.setDescription("Description");

        Materials material = new Materials();
        when(materialRepository.findByName(anyString())).thenReturn(material);
        when(checkConditionService.checkInputQuantity(any(Double.class))).thenReturn(true);
        when(checkConditionService.checkInputPrice(any(BigDecimal.class))).thenReturn(true);
        when(subMaterialsRepository.save(any(SubMaterials.class))).thenReturn(new SubMaterials());

        SubMaterials result = subMaterialServiceImpl.addNew(dto);

        assertNotNull(result);
        verify(materialRepository).findByName(anyString());
        verify(checkConditionService).checkInputQuantity(any(Double.class));
        verify(checkConditionService).checkInputPrice(any(BigDecimal.class));
        verify(subMaterialsRepository).save(any(SubMaterials.class));
        verify(inputSubMaterialRepository).save(any(InputSubMaterial.class));
    }

    @Test
    void testFilterByMaterial_EmptyList() {
        when(subMaterialsRepository.findSubMaterialIdByMaterial(anyInt())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(AppException.class, () -> {
            subMaterialServiceImpl.FilterByMaterial(1);
        });

        assertEquals(ErrorCode.NOT_FOUND, ((AppException) exception).getErrorCode());
        verify(subMaterialsRepository).findSubMaterialIdByMaterial(anyInt());
    }

    @Test
    void testAddNew_ValidInput() {
        SubMaterialDTO dto = new SubMaterialDTO();
        dto.setSub_material_name("SubMaterial1");
        dto.setMaterial_name("Material1");
        dto.setQuantity(10.0); // Use Double
        dto.setUnit_price(BigDecimal.valueOf(100));
        dto.setInput_price(BigDecimal.valueOf(50));
        dto.setDescription("Description");

        Materials material = new Materials();
        when(materialRepository.findByName(anyString())).thenReturn(material);
        when(checkConditionService.checkInputQuantity(any(Double.class))).thenReturn(true);
        when(checkConditionService.checkInputPrice(any(BigDecimal.class))).thenReturn(true);
        when(subMaterialsRepository.save(any(SubMaterials.class))).thenReturn(new SubMaterials());

        SubMaterials result = subMaterialServiceImpl.addNew(dto);

        assertNotNull(result);
        verify(materialRepository).findByName(anyString());
        verify(checkConditionService).checkInputQuantity(any(Double.class));
        verify(checkConditionService).checkInputPrice(any(BigDecimal.class));
        verify(subMaterialsRepository).save(any(SubMaterials.class));
        verify(inputSubMaterialRepository).save(any(InputSubMaterial.class));
    }

    @Test
    void testAddNew_InvalidQuantity() {

        SubMaterialDTO dto = new SubMaterialDTO();
        dto.setSub_material_name("aaaa");
        dto.setDescription("aa");
        dto.setUnit_price(BigDecimal.valueOf(100));
        dto.setInput_price(BigDecimal.valueOf(50));
        dto.setQuantity(-10.0); // Use Double

        when(checkConditionService.checkInputQuantity(any(Double.class))).thenReturn(false);

        Exception exception = assertThrows(AppException.class, () -> {
            subMaterialServiceImpl.addNew(dto);
        });

        assertEquals(ErrorCode.QUANTITY_INVALID, ((AppException) exception).getErrorCode());
        verify(checkConditionService).checkInputQuantity(any(Double.class));
    }


    @Test
    void testGenerateCode() {
        when(subMaterialsRepository.findSubMaterialsTop(anyString())).thenReturn(null);

        String code = subMaterialServiceImpl.generateCode();

        assertNotNull(code);
        assertTrue(code.startsWith(LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "SMR"));
        verify(subMaterialsRepository).findSubMaterialsTop(anyString());
    }

    @Test
    void testGenerateMultipleCode() {
        when(subMaterialsRepository.findSubMaterialsTop(anyString())).thenReturn(null);

        HashMap<Integer, String> codes = subMaterialServiceImpl.generateMultipleCode(5);

        assertEquals(5, codes.size());
        codes.values().forEach(code -> assertTrue(code.startsWith(LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyy")) + "SMR")));
        verify(subMaterialsRepository).findSubMaterialsTop(anyString());
    }

    @Test
    void testGetSubMaterialById_Found() {
        SubMaterialViewDTO dto = new SubMaterialViewDTO();
        when(subMaterialsRepository.findSubMaterialsById(anyInt())).thenReturn(dto);

        SubMaterialViewDTO result = subMaterialServiceImpl.getSubMaterialById(1);

        assertNotNull(result);
        verify(subMaterialsRepository).findSubMaterialsById(anyInt());
    }

    @Test
    void testGetSubMaterialById_NotFound() {
        when(subMaterialsRepository.findSubMaterialsById(anyInt())).thenReturn(null);

        Exception exception = assertThrows(AppException.class, () -> {
            subMaterialServiceImpl.getSubMaterialById(1);
        });

        assertEquals(ErrorCode.NOT_FOUND, ((AppException) exception).getErrorCode());
        verify(subMaterialsRepository).findSubMaterialsById(anyInt());
    }

    @Test
    void testGetProductSubMaterialByProductId() {
        List<Product_SubmaterialDTO> mockList = Arrays.asList(new Product_SubmaterialDTO());
        when(productSubMaterialsRepository.getProductSubMaterialByProductIdAndTypeMate(anyInt(), anyInt())).thenReturn(mockList);

        List<Product_SubmaterialDTO> result = subMaterialServiceImpl.getProductSubMaterialByProductId(1, 1);

        assertEquals(1, result.size());
        verify(productSubMaterialsRepository).getProductSubMaterialByProductIdAndTypeMate(anyInt(), anyInt());
    }

    @Test
    void testFindEmployeematerialsByName_NotFound() {
        when(employeeMaterialRepository.getAllEmployeeMate1Search(anyString())).thenReturn(Collections.emptyList());
        when(employeeMaterialRepository.getAllEmployeeMate2Search(anyString())).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(AppException.class, () -> {
            subMaterialServiceImpl.findEmployeematerialsByName("name");
        });

        assertEquals(ErrorCode.NOT_FOUND, ((AppException) exception).getErrorCode());

        // Verify individual repository calls
        verify(employeeMaterialRepository).getAllEmployeeMate1Search(anyString());
        verify(employeeMaterialRepository).getAllEmployeeMate2Search(anyString());
    }
//    @Test
//    void testUpdateSub() {
//        UpdateSubDTO updateSubDTO = new UpdateSubDTO();
//        updateSubDTO.setSub_material_name("UpdatedName");
//        updateSubDTO.setDescription("UpdatedDescription");
//        updateSubDTO.setQuantity(20.0); // Use Double
//        updateSubDTO.setUnit_price(BigDecimal.valueOf(200));
//
//        SubMaterials existingSubMaterial = new SubMaterials();
//        when(subMaterialsRepository.findById1(anyInt())).thenReturn(existingSubMaterial);
//      //  when(subMaterialsRepository.updateSubMaterials(anyInt(), anyString(), anyString(), any(Double.class), any(BigDecimal.class))).thenReturn(1);
//        when(inputSubMaterialRepository.save(any(InputSubMaterial.class))).thenReturn(new InputSubMaterial());
//
//        UpdateSubDTO result = subMaterialServiceImpl.UpdateSub(1, updateSubDTO);
//
//        assertNull(result);
//        verify(subMaterialsRepository).findById1(anyInt());
//        verify(subMaterialsRepository).updateSubMaterials(anyInt(), anyString(), anyString(), any(Double.class), any(BigDecimal.class));
//        verify(inputSubMaterialRepository).save(any(InputSubMaterial.class));
//    }
}