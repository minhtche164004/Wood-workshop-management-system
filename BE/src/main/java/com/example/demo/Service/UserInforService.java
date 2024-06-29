package com.example.demo.Service;

import com.example.demo.Entity.UserInfor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface UserInforService {
    UserInfor getUserInforByPhoneNumber(String phoneNumber);

    List<String> listPhoneNumberHasAccount();
}
