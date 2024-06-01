package com.example.demo.Service.Impl;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Repository.CategoryRepository;
import com.example.demo.Service.CategorySevice;
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
    private ModelMapper modelMapper;
    @Override
    public List<CategoryNameDTO> GetListName() {
        return categoryRepository.findAll().stream()
                .map(cate -> modelMapper.map(cate, CategoryNameDTO.class))
                .collect(Collectors.toList());
    }
}
