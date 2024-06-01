package com.example.demo.Config;

import com.example.demo.Dto.Category.CategoryNameDTO;
import com.example.demo.Dto.MaterialDTO.MaterialDTO;
import com.example.demo.Dto.ProductDTO.ProductDTO;
import com.example.demo.Dto.SubMaterialDTO.SubMaterialNameDTO;
import com.example.demo.Dto.SupplierDTO.SupplierNameDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Entity.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT); // Áp dụng chiến lược khớp chính xác

        modelMapper.typeMap(User.class, UserDTO.class)
                .addMapping(src -> src.getUserInfor().getFullname(), UserDTO::setFullname)
                .addMapping(User::getUsername, UserDTO::setUsername)
                .addMapping(User::getEmail, UserDTO::setEmail)
                .addMapping(src -> src.getUserInfor().getPhoneNumber(), UserDTO::setPhoneNumber)
                .addMapping(src -> src.getPosition().getPosition_name(), UserDTO::setPosition_name)
                .addMapping(src -> src.getUserInfor().getAddress(), UserDTO::setAddress)
                .addMapping(src -> src.getStatus().getStatus_name(), UserDTO::setStatus_name)
                .addMapping(src -> src.getRole().getRoleName(), UserDTO::setRole_name);// Ánh xạ roleName

//        modelMapper.typeMap(Products.class, ProductDTO.class)
//                .addMapping(Products::getProductName, ProductDTO::setProduct_name)
//                .addMapping(Products::getDescription, ProductDTO::setDescription)
//                .addMapping(Products::getQuantity, ProductDTO::setQuantity)
//                .addMapping(Products::getPrice, ProductDTO::setPrice)
//                .addMapping(src -> src.getStatus().getStatus_id(), ProductDTO::setStatus_id)
//                .addMapping(Products::getImage, ProductDTO::setImage)
//                .addMapping(Products::getType, ProductDTO::setType)
//                .addMapping(src -> src.getCategories().getCategoryId(), ProductDTO::setCategory_id);


        modelMapper.typeMap(SubMaterials.class, SubMaterialNameDTO.class)
                .addMapping(SubMaterials::getSubMaterialName, SubMaterialNameDTO::setSub_material_name);
        modelMapper.typeMap(Materials.class, MaterialDTO.class)
                .addMapping(Materials::getMaterialName, MaterialDTO::setMaterialName);
        modelMapper.typeMap(Suppliermaterial.class, SupplierNameDTO.class)
                .addMapping(Suppliermaterial::getSupplierName, SupplierNameDTO::setSupplierName);
        modelMapper.typeMap(Categories.class, CategoryNameDTO.class)
                .addMapping(Categories::getCategoryName, CategoryNameDTO::setCategoryName);

        return modelMapper;
    }

}