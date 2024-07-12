package com.example.demo.Service;

import com.example.demo.Entity.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Pattern;

@Service
public interface CheckConditionService {
    boolean checkPasswordUser(String email, String password);


    boolean checkUserbyUsername(String username);

    boolean checkUserbyEmail(String email);

    boolean checkEmail(String email);
    boolean checkPhone(String phone);

    boolean checkName(String name);

    boolean checkAddress(String name);

    boolean checkPhoneNumber(String name);

    boolean checkFullName(String name);

    boolean checkInputName(String name);

     boolean checkInputQuantity(Double number);
    boolean checkInputQuantityInt(int number);

   boolean checkInputPrice(BigDecimal number);
    boolean checkInputQuantityIntForProductError(int number,int current);

}
