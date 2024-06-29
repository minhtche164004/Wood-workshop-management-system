package com.example.demo.Config;

import com.example.demo.Dto.SubMaterialDTO.UpdateSubDTO;
import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
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
                .addMapping(User::getUserId, UserDTO::setUserId)
                .addMapping(src -> src.getUserInfor().getFullname(), UserDTO::setFullname)
                .addMapping(User::getUsername, UserDTO::setUsername)
                .addMapping(User::getEmail, UserDTO::setEmail)
                .addMapping(src -> src.getUserInfor().getPhoneNumber(), UserDTO::setPhoneNumber)
                .addMapping(src -> src.getPosition().getPosition_name(), UserDTO::setPosition_name)
                .addMapping(src -> src.getUserInfor().getAddress(), UserDTO::setAddress)
                .addMapping(src -> src.getStatus().getStatus_name(), UserDTO::setStatus_name)
                .addMapping(src -> src.getUserInfor().getBank_number(), UserDTO::setBank_number)
                .addMapping(src -> src.getUserInfor().getBank_name(), UserDTO::setBank_name)
                .addMapping(src -> src.getUserInfor().getWards(), UserDTO::setWards)
                .addMapping(src -> src.getUserInfor().getDistrict(), UserDTO::setDistrict)
                .addMapping(src -> src.getUserInfor().getCity_province(), UserDTO::setCity_province)
                .addMapping(src -> src.getRole().getRoleName(), UserDTO::setRole_name);// Ánh xạ roleName

        modelMapper.typeMap(User.class, UpdateProfileDTO.class)
                .addMapping(src -> src.getUserInfor().getBank_name(), UpdateProfileDTO::setBank_name)
                .addMapping(src -> src.getUserInfor().getBank_number(), UpdateProfileDTO::setBank_number)
                .addMapping(User::getUsername, UpdateProfileDTO::setUsername)
                .addMapping(User::getEmail, UpdateProfileDTO::setEmail)
                .addMapping(src -> src.getUserInfor().getFullname(), UpdateProfileDTO::setFullname)
                .addMapping(src -> src.getUserInfor().getPhoneNumber(), UpdateProfileDTO::setPhoneNumber)
                .addMapping(src -> src.getUserInfor().getAddress(), UpdateProfileDTO::setAddress)
                .addMapping(src -> src.getUserInfor().getCity_province(), UpdateProfileDTO::setCity)
                .addMapping(src -> src.getUserInfor().getDistrict(), UpdateProfileDTO::setDistrict)
                .addMapping(src -> src.getUserInfor().getWards(), UpdateProfileDTO::setWards);


        modelMapper.typeMap(SubMaterials.class, UpdateSubDTO.class)
                .addMapping(SubMaterials::getSubMaterialName, UpdateSubDTO::setSub_material_name)
                .addMapping(SubMaterials::getDescription, UpdateSubDTO::setDescription)
                .addMapping(SubMaterials::getQuantity, UpdateSubDTO::setQuantity)
                .addMapping(SubMaterials::getUnitPrice, UpdateSubDTO::setUnit_price);





//        modelMapper.typeMap(Products.class, ProductDTO_Show.class)
//                .addMapping(Products::getProductName, ProductDTO_Show::setProduct_name)
//                .addMapping(Products::getDescription, ProductDTO_Show::setDescription)
//                .addMapping(Products::getQuantity, ProductDTO_Show::setQuantity)
//                .addMapping(Products::getPrice, ProductDTO_Show::setPrice)
//                .addMapping(src -> src.getStatus().getStatus_id(), ProductDTO_Show::setStatus_id)
//                .addMapping(Products::getImage, ProductDTO_Show::setImages)
//                .addMapping(Products::getType, ProductDTO_Show::setType)
//                .addMapping(src -> src.getCategories().getCategoryId(), ProductDTO_Show::setCategory_id);
        return modelMapper;
    }

}