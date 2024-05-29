package com.example.demo.Config;

import com.example.demo.Dto.TestDTO1;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Entity.User;
import org.eclipse.angus.mail.imap.protocol.UID;
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

        modelMapper.typeMap(User.class, TestDTO1.class)
                .addMapping(User::getUsername, TestDTO1::setUsername)
                .addMapping(User::getEmail, TestDTO1::setEmail)
                .addMapping(src -> src.getRole().getRoleName(), TestDTO1::setRole_name); // Ánh xạ roleName

//        modelMapper.typeMap(User.class, TestDTO1.class)
//                .addMapping(User::getUsername, TestDTO1::setUsername)
//                .addMapping(User::getEmail, TestDTO1::setEmail);

        return modelMapper;
    }

}