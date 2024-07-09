package com.example.demo.Service.Impl;

import com.example.demo.Dto.UserDTO.UserInforDTO;
import com.example.demo.Entity.UserInfor;
import com.example.demo.Repository.InformationUserRepository;
import com.example.demo.Service.UserInforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInforServiceImpl implements UserInforService {
    @Autowired
    InformationUserRepository informationUserRepository;

    @Override
    public UserInforDTO getUserInforByPhoneNumber(String phoneNumber) {
        UserInforDTO userInfor = informationUserRepository.findUsersByPhoneNumber(phoneNumber);
        return userInfor;
    }

    @Override
    public List<String> listPhoneNumberHasAccount() {
        List<String> userInfors = informationUserRepository.listPhoneNumberHasAccount();
        return userInfors;
    }
}
