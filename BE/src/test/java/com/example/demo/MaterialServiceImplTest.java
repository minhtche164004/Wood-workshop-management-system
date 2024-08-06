package com.example.demo;

import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Entity.Materials;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;

import com.example.demo.Repository.MaterialRepository;
import com.example.demo.Service.CheckConditionService;

import com.example.demo.Service.Impl.MaterialServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
public class MaterialServiceImplTest {
    @Mock
    private MaterialRepository materialRepository;

    @Mock
    private CheckConditionService checkConditionService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private MaterialServiceImpl materialServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewMaterial_ValidInput_Success() {
        // Arrange
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("Valid Material");
        materialDTO.setType("Type");

        when(checkConditionService.checkInputName(materialDTO.getMaterialName())).thenReturn(true);
        when(materialRepository.countByMaterialName(materialDTO.getMaterialName())).thenReturn(0);

        // Act
        Materials result = materialServiceImpl.AddNewMaterial(materialDTO);

        // Assert
        assertEquals("Valid Material", result.getMaterialName());
        assertEquals("Type", result.getType());
        verify(materialRepository).save(any(Materials.class));
    }

    @Test
    void testAddNewMaterial_InvalidName() {
        // Arrange
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("Invalid Name!");
        materialDTO.setType("Type");

        when(checkConditionService.checkInputName(materialDTO.getMaterialName())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.AddNewMaterial(materialDTO));
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode()); // Adjust if you handle this in your exception class
    }

    @Test
    void testAddNewMaterial_NameExists() {
        // Arrange
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("n");
        materialDTO.setType("Type");

        when(checkConditionService.checkInputName(materialDTO.getMaterialName())).thenReturn(true);
        when(materialRepository.countByMaterialName(materialDTO.getMaterialName())).thenReturn(1);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.AddNewMaterial(materialDTO));
        assertEquals(ErrorCode.NAME_EXIST, thrown.getErrorCode()); // Adjust if you handle this in your exception class
    }

    @Test
    void testAddNewMaterial_EmptyName() {
        // Arrange
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName(""); // Empty name
        materialDTO.setType("Type");

        when(checkConditionService.checkInputName(materialDTO.getMaterialName())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.AddNewMaterial(materialDTO));
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode()); // Adjust if you handle this in your exception class
    }

    @Test
    void testAddNewMaterial_NullInput() {
        // Arrange
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName(null); // Null name
        materialDTO.setType("Type");

        when(checkConditionService.checkInputName(materialDTO.getMaterialName())).thenReturn(false);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.AddNewMaterial(materialDTO));
        assertEquals(ErrorCode.INVALID_FORMAT_NAME, thrown.getErrorCode()); // Adjust if you handle this in your exception class
    }
    @Test
    void testGetListName_Success() {
        // Arrange
        List<Materials> materialsList = new ArrayList<>();
        Materials material = new Materials();
        materialsList.add(material);

        MaterialDTO materialDTO = new MaterialDTO();
        when(materialRepository.findAll()).thenReturn(materialsList);
        when(modelMapper.map(material, MaterialDTO.class)).thenReturn(materialDTO);

        // Act
        List<MaterialDTO> result = materialServiceImpl.GetListName();

        // Assert
        assertEquals(1, result.size());
        assertEquals(materialDTO, result.get(0));
    }

    @Test
    void testGetListName_EmptyList() {
        // Arrange
        when(materialRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<MaterialDTO> result = materialServiceImpl.GetListName();

        // Assert
        assertTrue(result.isEmpty());
    }

    // Test cases for EditMaterial
    @Test
    void testEditMaterial_Success() {
        // Arrange
        int id = 1;
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("New Material");
        materialDTO.setType("Type");

        Materials updatedMaterial = new Materials();
        when(materialRepository.findById1(id)).thenReturn(updatedMaterial);

        // Act
        Materials result = materialServiceImpl.EditMaterial(id, materialDTO);

        // Assert
        verify(materialRepository).updateMaterial(id, materialDTO.getMaterialName(), materialDTO.getType());
        assertEquals(updatedMaterial, result);
    }

    @Test
    void testEditMaterial_Failure() {
        // Arrange
        int id = 1;
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("New Material");
        materialDTO.setType("Type");

        when(materialRepository.findById1(id)).thenReturn(null);

        // Act
        Materials result = materialServiceImpl.EditMaterial(id, materialDTO);

        // Assert
        assertNull(result);
    }

    // Test cases for GetMaterialById
    @Test
    void testGetMaterialById_Found() {
        // Arrange
        int id = 1;
        Materials material = new Materials();
        when(materialRepository.findById1(id)).thenReturn(material);

        // Act
        Materials result = materialServiceImpl.GetMaterialById(id);

        // Assert
        assertEquals(material, result);
    }

    @Test
    void testGetMaterialById_NotFound() {
        // Arrange
        int id = 1;
        when(materialRepository.findById1(id)).thenReturn(null);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.GetMaterialById(id));
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }




    @Test
    void testGetListName_ModelMapperException() {
        // Arrange
        List<Materials> materialsList = new ArrayList<>();
        Materials material = new Materials();
        materialsList.add(material);

        when(materialRepository.findAll()).thenReturn(materialsList);
        when(modelMapper.map(material, MaterialDTO.class)).thenThrow(new RuntimeException("Mapping error"));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> materialServiceImpl.GetListName());
        assertEquals("Mapping error", thrown.getMessage());
    }

    @Test
    void testEditMaterial_InvalidData() {
        // Arrange
        int id = 1;
        MaterialDTO materialDTO = new MaterialDTO();
        materialDTO.setMaterialName("123"); // Invalid data
        materialDTO.setType("Invalid Type"); // Potentially invalid type if not accepted

        // Mock the repository to return a valid material object since the ID exists
        Materials existingMaterial = new Materials();
        when(materialRepository.findById1(id)).thenReturn(existingMaterial);

        // Mock the update method if necessary
        doNothing().when(materialRepository).updateMaterial(id, materialDTO.getMaterialName(), materialDTO.getType());

        // Act
        Materials result = materialServiceImpl.EditMaterial(id, materialDTO);

        // Assert
        assertNotNull(result); // Ensure that a non-null result is returned

    }


    @Test
    void testGetMaterialById_NullId() {
        // Arrange
        int id = 0; // Assuming 0 is an invalid ID
        when(materialRepository.findById1(id)).thenReturn(null);

        // Act & Assert
        AppException thrown = assertThrows(AppException.class, () -> materialServiceImpl.GetMaterialById(id));
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }
}

