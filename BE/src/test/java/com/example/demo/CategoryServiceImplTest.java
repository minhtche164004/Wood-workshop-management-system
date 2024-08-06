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

//    @Test
//    public void testAddNewCategory_Success() {
//        // Tạo dữ liệu mẫu
//        String newCategoryName = "Test Category";
//        CategoryNameDTO categoryNameDTO = new CategoryNameDTO(newCategoryName);
//
//        // Mock behavior của categoryRepository (tùy thuộc vào cách bạn mock)
//        when(categoryRepository.findByCategoryName(newCategoryName)).thenReturn(null); // Không tìm thấy category trùng tên
//
//        // Gọi phương thức cần test
//        categoryServiceImpl.AddnewCategory(categoryNameDTO);
//
//        // Kiểm tra kết quả (sử dụng Mockito verify)
//        verify(categoryRepository).save(any(Categories.class)); // Kiểm tra xem categoryRepository.save được gọi
//    }

    @Test
    void testAddNewCategory_ShouldThrowAppException_WhenCategoryNameExists() {
        //tạo dữ liệu mẫu
        CategoryNameDTO categoryNameDTO = new CategoryNameDTO();
        categoryNameDTO.setCategoryName("Ghế");
        //thiết lập 1 cuộc gọi giả đến phương thức findByCategoryName
        when(categoryRepository.findByCategoryName("Ghế")).thenReturn(new Categories());

        AppException exception = assertThrows(AppException.class, () -> {
            categoryServiceImpl.AddnewCategory(categoryNameDTO);
        });
        //nếu ko ném ra ngoại lệ thì test sai
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
        //tạo 1 id không tồn tại
        int nonExistentId = 100;
        when(categoryRepository.findById(nonExistentId)).thenReturn(null); //để mô phỏng trường hợp không tìm thấy category có id là nonExistentId trong cơ sở dữ liệu.
        //Gọi phương thức findById của categoryRepository với id không tồn tại.
        Categories result = categoryRepository.findById(nonExistentId);

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
