package com.example.demo.Service;

import com.example.demo.Dto.Category.CategoryNameDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategorySevice {
    List<CategoryNameDTO> GetListName();
}
