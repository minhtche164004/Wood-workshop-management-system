package com.example.demo.Service.Impl;

import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.CheckConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.regex.Pattern;
@Service
public class CheckConditionsImpl implements CheckConditionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean checkUserbyUsername(String username) {
        return !userRepository.findByUsername(username).isPresent();
    }
    public boolean checkUserbyEmail(String email) {
        return !userRepository.findByEmail(email).isPresent();
    }
    public boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$");
        return p.matcher(email).find();
    }
    public boolean checkName(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9 ]+$");
        return p.matcher(name).find();
    }
    public boolean checkAddress(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9À-ỹ\\s]+$");
        return p.matcher(name).find();
    }
    public boolean checkPhoneNumber(String name) {
        Pattern p = Pattern.compile("^[0-9]+$");
        return p.matcher(name).find();
    }
    public boolean checkFullName(String name) {
        Pattern p = Pattern.compile("^[a-zA-ZÀ-ỹ\\s]+$");
        return p.matcher(name).find();
    }
    public boolean checkInputName(String name) {
        Pattern p = Pattern.compile("^[a-zA-ZÀ-ỹ0-9\\s]+$"); // Chấp nhận cả dấu tiếng Việt và khoảng trắng và số
        return p.matcher(name).find();
    }

    public boolean checkInputQuantity(Double number) {
        return number > 0; // Kiểm tra trực tiếp xem số có lớn hơn 0 hay không
    }
    public boolean checkInputQuantityInt(int number) {
        return number > 0; // Kiểm tra trực tiếp xem số có lớn hơn 0 hay không
    }
    public boolean checkInputPrice(BigDecimal number) {
        return number.compareTo(BigDecimal.ZERO) > 0;
    }
    public boolean checkPasswordUser(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return false;
        }
        return true;
    }
}
