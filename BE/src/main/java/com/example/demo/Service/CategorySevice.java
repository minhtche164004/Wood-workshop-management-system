package com.example.demo.Service;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Entity.Categories;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategorySevice {
    List<CategoryNameDTO> GetListName();
    List<Categories> GetAllCategoty();
    void AddnewCategory(CategoryNameDTO categoryNameDTO);
    Categories UpdateCategoty(int id, CategoryNameDTO categoryNameDTO);
    void DeleteCategory(int id);
    List<Categories> findCategoriesByName(String key);
}
