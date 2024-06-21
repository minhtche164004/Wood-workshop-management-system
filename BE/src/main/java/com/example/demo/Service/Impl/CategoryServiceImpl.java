package com.example.demo.Service.Impl;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Entity.Categories;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.CategorySevice;
import com.example.demo.Service.CheckConditionService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategorySevice {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EntityManager entityManager;
    @Override
    public List<CategoryNameDTO> GetListName() {
        return categoryRepository.findAll().stream()
                .map(cate -> modelMapper.map(cate, CategoryNameDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public void AddnewCategory(CategoryNameDTO categoryNameDTO) {
        if(categoryRepository.findByCategoryName(categoryNameDTO.getCategoryName()) != null){
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        Categories categories = new Categories();
        categories.setCategoryName(categoryNameDTO.getCategoryName());
        categoryRepository.save(categories);
    }
    @Transactional
    @Override
    public Categories UpdateCategoty(int id,CategoryNameDTO categoryNameDTO){
        Categories categories = categoryRepository.findById(id);
        if(!categoryNameDTO.getCategoryName().equals(categories.getCategoryName()) &&
                categoryRepository.findByCategoryName(categoryNameDTO.getCategoryName()) != null){
            throw new AppException(ErrorCode.NAME_EXIST);
        }
        categories.setCategoryName(categoryNameDTO.getCategoryName());
        categoryRepository.save(categories);
        entityManager.refresh(categories);
        return categories;
    }


}
