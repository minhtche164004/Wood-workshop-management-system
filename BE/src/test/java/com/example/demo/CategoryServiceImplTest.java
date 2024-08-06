package com.example.demo;


import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Entity.Categories;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.Impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CheckConditionService checkConditionService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EntityManager entityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewCategory_ShouldThrowAppException_WhenCategoryNameExists() {
        CategoryNameDTO categoryNameDTO = new CategoryNameDTO();
        categoryNameDTO.setCategoryName("Ghế");
        when(categoryRepository.findByCategoryName("Ghế")).thenReturn(new Categories());

        AppException exception = assertThrows(AppException.class, () -> {
            categoryServiceImpl.AddnewCategory(categoryNameDTO);
        });

        assertEquals(ErrorCode.NAME_EXIST, exception.getErrorCode());
    }

    @Test
    void testAddNewCategory_ShouldThrowAppException_WhenCategoryNameIsNull() {
        CategoryNameDTO categoryNameDTO = new CategoryNameDTO();
        categoryNameDTO.setCategoryName(null);

        AppException exception = assertThrows(AppException.class, () -> {
            categoryServiceImpl.AddnewCategory(categoryNameDTO);
        });

        assertEquals(ErrorCode.MUST_REQUIRED, exception.getErrorCode());
    }

    @Test
    void testUpdateCategory_ShouldThrowAppException_WhenCategoryNameExists() {
        CategoryNameDTO categoryNameDTO = new CategoryNameDTO();
        categoryNameDTO.setCategoryName("Ghế");
        Categories existingCategory = new Categories();
        existingCategory.setCategoryName("Bàn");

        when(categoryRepository.findById(1)).thenReturn(existingCategory);
        when(categoryRepository.findByCategoryName("Ghế")).thenReturn(new Categories());

        AppException exception = assertThrows(AppException.class, () -> {
            categoryServiceImpl.UpdateCategoty(1, categoryNameDTO);
        });

        assertEquals(ErrorCode.NAME_EXIST, exception.getErrorCode());
    }

    @Test
    void testFindById_WhenIdDoesNotExist() {
        // Arrange
        int nonExistentId = 100;
        when(categoryRepository.findById(nonExistentId)).thenReturn(null);

        // Act
        Categories result = categoryRepository.findById(nonExistentId);

        // Assert
        assertNull(result, "Expected findById to return null for a non-existent ID");
    }

    @Test
    void testFindById_WhenNameDoesNotExist() {
        // Arrange
        String name = "Testing";
        when(categoryRepository.findByCategoryName(name)).thenReturn(null);

        // Act
        Categories result = categoryRepository.findByCategoryName(name);

        // Assert
        assertNull(result, "Expected findById to return null for a non-existent Name");
    }

    @Test
    void testFindById_WhenNameisNullExist() {
        // Arrange
        String name = "";
        when(categoryRepository.findByCategoryName(name)).thenReturn(null);

        // Act
        Categories result = categoryRepository.findByCategoryName(name);

        // Assert
        assertNull(result, "Expected findByCategoryName to return null for a non-existent Name");
    }


}
