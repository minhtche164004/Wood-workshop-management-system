package com.example.demo.Service;

import com.example.demo.Dto.UserDTO.UserInforDTO;
import com.example.demo.Entity.UserInfor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserInforService {
    UserInforDTO getUserInforByPhoneNumber(String phoneNumber);

    List<String> listPhoneNumberHasAccount();
}
