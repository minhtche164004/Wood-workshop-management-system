package com.example.demo.Service.Impl;

import com.example.demo.Dto.TestDTO;
import com.example.demo.Dto.TestDTO1;
import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.*;
import com.example.demo.Jwt.UserDetailsServiceImpl;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Repository.*;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.UserService;
import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private InformationUserRepository informationUserRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ModelMapper modelMapper;

@Override
    public void checkConditions(UserDTO userDTO) { //check các điều kiện cho form Register
        if (!checkEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
        if (!checkName(userDTO.getUsername())) {
            throw new AppException(ErrorCode.INVALID_NAME_FORMAT);
        }
        if (!userDTO.getPassword().equals(userDTO.getCheckPass())) {
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        if (!checkUserbyEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
    }
   // @Transactional
    @Override
    public User signup(UserDTO userDTO){
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass = passwordEncoder.encode(userDTO.getPassword());
        // Tạo vai trò người dùng mặc định
        Role userRole = roleRepository.findByName("CUSTOMER");
        Status status= statusRepository.findByName("Active");
Position position = positionRepository.findByName("Not a worker");
        UserInfor userInfor = new UserInfor(
                userDTO.getFullname(),
                userDTO.getPhoneNumber(),
                userDTO.getAddress()
        );


        informationUserRepository.save(userInfor);
        User user = new User(
                                0,
                                userDTO.getUsername(),
                                pass,
                                userDTO.getEmail(),
                                status,
                position,
                hireDate,
                userRole,
                userInfor

                        );
   //     user.setUserInfor(userInfor);
        // Lưu người dùng vào cơ sở dữ liệu và trả về người dùng mới
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest loginRequest){
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }
        User a = userRepository.getUserByEmail(user.getUsername());
      if(a.getStatus().getStatus_id()==1){
         throw new AppException(ErrorCode.UN_ACTIVE_ACCOUNT);
}
        // Kiểm tra xem mật khẩu nhập vào có khớp với mật khẩu lưu trong cơ sở dữ liệu không
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }
        // Nếu mọi thứ đều đúng, tạo JWT token và trả về
        var jwt = jwtService.generateToken(new HashMap<>(), user);
       // var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
        jwtAuthenticationResponse.setRefreshToken("");
        jwtAuthenticationResponse.setUser(user);
        return jwtAuthenticationResponse;
    }


    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
     //   User user = userRepository.findByEmail(userEmail).orElseThrow();
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(userEmail);
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }

        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(new HashMap<>(),user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(refreshTokenRequest.getToken());
            jwtAuthenticationResponse.setRefreshToken("");
            return jwtAuthenticationResponse;
        }
        return null;
    }
    public Boolean checkPasswordUser(String email, String password) {
   User user = userRepository.findByEmail(email).orElse(null);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return false;
        }
        return true;
    }
    public Boolean checkUserbyEmail(String email) {

        return !userRepository.findByEmail(email).isPresent();
    }
    public Boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+(\\.[a-zA-Z]+)+$");
        return p.matcher(email).find();
    }
    public Boolean checkName(String name) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9 ]+$");
        return p.matcher(name).find();
    }
    @Override
    public User getUserbyEmail(String email) {
        return userRepository.getUserByEmail(email);
    }



    @Override
    public List<UserUpdateDTO> GetAllUser(){
        List<User> userList = userRepository.findAll();
        List<UserUpdateDTO> userUpdateDTOS = new ArrayList<>();
        for(User user : userList){
            UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
      //      userUpdateDTO.setFullname(user.getUserInfor().getFullname().toString());
            userUpdateDTO.setEmail(user.getEmail().toString());
            userUpdateDTO.setAddress(user.getUserInfor().getAddress().toString());
         //   userDTO.setPosition(user.getPosition().getPosition_name().toString());
                userUpdateDTO.setPosition(user.getPosition().getPosition_name());
            userUpdateDTO.setRole(user.getRole().getRoleName().toString());
            userUpdateDTO.setStatus(user.getStatus().getStatus_name().toString());
          //  userUpdateDTO.setPhoneNumber(user.getUserInfor().getPhoneNumber().toString());
            userUpdateDTO.setUsername(user.getUsername().toString());
            // Gán các giá trị khác tương ứng từ user sang userDTO
            userUpdateDTOS.add(userUpdateDTO);
        }

        return userUpdateDTOS;
    }

    @Override
    public UserUpdateDTO GetUserById(int user_id) {
        Optional<UserUpdateDTO> userOptional = userRepository.findByIdTest1(user_id);
        return userOptional
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));
    }
    @Override
    public TestDTO1 FindbyId(int user_id) {
        Optional<User> userOptional = userRepository.findById(user_id); // Lấy Optional<User> từ repository
        return userOptional.map(user -> modelMapper.map(user, TestDTO1.class))
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));
    }

}


