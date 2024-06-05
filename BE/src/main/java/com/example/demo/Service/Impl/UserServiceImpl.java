package com.example.demo.Service.Impl;

import com.example.demo.Dto.UserDTO.RegisterDTO;
import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Entity.*;
import com.example.demo.Jwt.UserDetailsServiceImpl;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Repository.*;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    @Autowired
    private UserInforRepository userInforRepository;
    @Autowired
    private CheckConditionService checkConditionService;


    @Override
    public User signup(RegisterDTO userDTO){
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass = passwordEncoder.encode(userDTO.getPassword());
        Role userRole = roleRepository.findById(2); //Default la CUSTOMER
        Status status= statusRepository.findById(2); //Default la KICHHOAT
Position position = positionRepository.findById(1);//Default la khong phai employee
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
        return userRepository.save(user);
    }

    @Override
    public User CreateAccountForAdmin(RegisterDTO userDTO) {
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass = passwordEncoder.encode(userDTO.getPassword());
        Role userRole = roleRepository.findById(userDTO.getRole());
        Status status= statusRepository.findById(2);
        Position position = positionRepository.findById(userDTO.getPosition());
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
        return userRepository.save(user);
    }


//    @Override
//    public User EditStatus(int userId) {
//
//    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest loginRequest){
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_PASS_OR_EMAIL);
        }
        User a = userRepository.getUserByUsername(user.getUsername());
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

    @Override
    public void checkConditions(RegisterDTO userDTO) { //check các điều kiện cho form Register
        if (!checkConditionService.checkEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
        if (!checkConditionService.checkName(userDTO.getUsername())) {
            throw new AppException(ErrorCode.INVALID_NAME_FORMAT);
        }
        if (!userDTO.getPassword().equals(userDTO.getCheckPass())) {
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        if (!checkConditionService.checkUserbyEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if(!checkConditionService.checkUserbyUsername(userDTO.getUsername())){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

    }

//    @Override
//    public User getUserbyEmail(String email) {
//        return userRepository.getUserByEmail(email);
//    }

    @Override
    public List<UserDTO> GetAllUser() {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public UserDTO FindbyId(int user_id) {
        Optional<User> userOptional = userRepository.findById(user_id);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return modelMapper.map(user, UserDTO.class); // Ánh xạ User sang TestDTO1
    }

    @Override
    public List<UserDTO> FindByUsernameOrAddress(String key) {
        List<User> userList = userRepository.findByUsernameOrAddress(key);
        if (userList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public UserUpdateDTO GetUserById(int user_id) {
        Optional<UserUpdateDTO> userOptional = userRepository.findByIdTest1(user_id);
        return userOptional
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));
    }

    @Override
    public User FindbyId1(int user_id) {
        Optional<User> userOptional = userRepository.findById(user_id); // Lấy Optional<User> từ repository
        return userOptional
                .orElseThrow(() ->  new AppException(ErrorCode.NOT_FOUND));
    }


    @Override
    public UserDTO EditUser(int userId,UpdateProfileDTO updateProfileDTO){
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if (!checkConditionService.checkAddress(updateProfileDTO.getAddress())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_ADDRESS);
        }
        if (!checkConditionService.checkPhoneNumber(updateProfileDTO.getPhoneNumber())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONE_NUMBER);
        }
        if (!checkConditionService.checkFullName(updateProfileDTO.getFullname())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (userRepository.countByEmail(updateProfileDTO.getEmail()) > 0) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if(userRepository.countByUsername(updateProfileDTO.getUsername()) > 0){
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }

        user.getUserInfor().setAddress(updateProfileDTO.getAddress());
        user.getUserInfor().setFullname(updateProfileDTO.getFullname());
        user.setUsername(updateProfileDTO.getUsername());
        user.getUserInfor().setPhoneNumber(updateProfileDTO.getPhoneNumber());
        user.setEmail(updateProfileDTO.getEmail());
        userRepository.save(user);
        return modelMapper.map(user, UserDTO.class);
    }
    @Transactional
    //Đảm bảo tính toàn vẹn dữ liệu, nếu có lỗi thì tất cả các thao tác sẽ được rollback (hoàn tác)
    @Override
    public void DeleteUserById(int UserId){
        User user= userRepository.findById(UserId).get();
        int info_id = user.getUserInfor().getInforId();
       userInforRepository.deleteById(info_id);
       userRepository.DeleteById(UserId);
    }

}


