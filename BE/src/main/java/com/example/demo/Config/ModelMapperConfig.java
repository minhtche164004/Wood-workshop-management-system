package com.example.demo.Config;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
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

//        modelMapper.typeMap(User.class, TestDTO1.class)
//                .addMapping(User::getUsername, TestDTO1::setUsername)
//                .addMapping(User::getEmail, TestDTO1::setEmail);

        return modelMapper;
    }

}