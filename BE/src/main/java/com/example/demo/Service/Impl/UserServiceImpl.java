package com.example.demo.Service.Impl;

import com.example.demo.Dto.UserDTO.ChangePassDTO;
import com.example.demo.Dto.UserDTO.*;
import com.example.demo.Entity.*;
import com.example.demo.Jwt.UserDetailsServiceImpl;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Jwt.RefreshTokenRequest;
import com.example.demo.Repository.*;
import com.example.demo.Request.LoginRequest;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.JWTService;
import com.example.demo.Service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.modelmapper.ModelMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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
    private Status_User_Repository statusRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private InformationUserRepository informationUserRepository;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ModelMapper modelMapper;
//    @Autowired
//    private UserInforRepository userInforRepository;
    @Autowired
    private CheckConditionService checkConditionService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private JobRepository jobRepository;


    @Override
    public User signup(RegisterDTO userDTO) {
        // Lấy thời gian hiện tại
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass = passwordEncoder.encode(userDTO.getPassword());
        Role userRole = roleRepository.findById(2); //Default la CUSTOMER
        Status_User status = statusRepository.findById(2); //Default la KICHHOAT
        Position position = positionRepository.findById(4);//Default la Không đảm nhận vị trí
        UserInfor userInfor = new UserInfor(
                userDTO.getPhoneNumber().trim(),
                userDTO.getFullname().trim(),
                userDTO.getAddress(),
                "",
                "",
                userDTO.getCity(),
                userDTO.getDistrict(),
                userDTO.getWards(),
                1
        );
        informationUserRepository.save(userInfor);
        User user = new User(
                0,
                userDTO.getUsername().trim(),
                pass,
                userDTO.getEmail().trim(),
                status,
                position,
                hireDate,
                userRole,
                userInfor

        );
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtService.extractUserName(refreshTokenRequest.getToken());
        UserDetails user =userDetailsService.loadUserByUsername(userEmail);
        if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtService.generateToken(new HashMap<>(),user);
            JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
            jwtAuthenticationResponse.setToken(jwt);
            jwtAuthenticationResponse.setUser(user);
            return jwtAuthenticationResponse;
        }
        return null;
    }

    @Override
    public User CreateAccountForAdmin(User_Admin_DTO userDTO) {
        //admin có thể set đc position , role,
        // Lấy thời gian hiện tại
        if (!checkConditionService.checkUserbyUsername(userDTO.getUsername())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (!checkConditionService.checkEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
        if (!checkConditionService.checkUserbyEmail(userDTO.getEmail())) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if (!checkConditionService.checkPhone(userDTO.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        if (!checkConditionService.checkPhoneNumber(userDTO.getPhoneNumber())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONE_NUMBER);
        }
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Chuyển đổi từ LocalDateTime sang java.util.Date
        Date hireDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        String pass = passwordEncoder.encode(userDTO.getPassword());
        Role userRole = roleRepository.findById(userDTO.getRole());
        Status_User status = statusRepository.findById(2);
        Position position = positionRepository.findById(userDTO.getPosition());
        UserInfor userInfor = new UserInfor(
                userDTO.getPhoneNumber().trim(),
                userDTO.getFullname().trim(),
                userDTO.getAddress(),
                userDTO.getBank_name(),
                userDTO.getBank_number(),
                userDTO.getCity(),
                userDTO.getDistrict(),
                userDTO.getWards(),
                1 //register thì cho has_Account là 1 , nghĩa là đã có account

        );

//        if (!checkConditionService.checkEmail(userDTO.getFullname())) {
//            throw new AppException(ErrorCode.INVALID_FULL_NAME);
//        }
        informationUserRepository.save(userInfor);
        User user = new User(
                0,
                userDTO.getUsername().trim(),
                pass,
                userDTO.getEmail().trim(),
                status,
                position,
                hireDate,
                userRole,
                userInfor

        );
        return userRepository.save(user);
    }

    @Override
    public JwtAuthenticationResponse signin(LoginRequest loginRequest) {
        UserDetails user;
        try {
            user = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new AppException(ErrorCode.WRONG_USER_NAME);
        }
        User a = userRepository.getUserByUsername(user.getUsername());
        if (a.getStatus().getStatus_id() == 1) {
            throw new AppException(ErrorCode.UN_ACTIVE_ACCOUNT);
        }
        // Kiểm tra xem mật khẩu nhập vào có khớp với mật khẩu lưu trong cơ sở dữ liệu không
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASS);
        }

        // Nếu mọi thứ đều đúng, tạo JWT token và trả về
        var jwt = jwtService.generateToken(new HashMap<>(), user);
        // var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse();
        jwtAuthenticationResponse.setToken(jwt);
      //  jwtAuthenticationResponse.setRefreshToken("");

        jwtAuthenticationResponse.setUser(user);
        return jwtAuthenticationResponse;
    }

    @Override
    public void changePass(ChangePassDTO changePassDTO) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername().trim();
        User user = userRepository.getUserByUsername(username);
        if (!passwordEncoder.matches(changePassDTO.getOld_pass(),user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASS);
        }
        if (!changePassDTO.getCheck_pass().equals(changePassDTO.getNew_pass())){
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        String pass = passwordEncoder.encode(changePassDTO.getNew_pass().trim());
        userRepository.updatePassword(user.getEmail(),pass);
    }





    @Override
    public void checkConditions(RegisterDTO userDTO) { //check các điều kiện cho form Register
        if (!checkConditionService.checkPhone(userDTO.getPhoneNumber().trim())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }
        if (!checkConditionService.checkEmail(userDTO.getEmail().trim())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
        if (!checkConditionService.checkName(userDTO.getUsername().trim())) {
            throw new AppException(ErrorCode.INVALID_NAME_FORMAT);
        }
        if (!userDTO.getPassword().equals(userDTO.getCheckPass().trim())) {
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        if (!checkConditionService.checkUserbyEmail(userDTO.getEmail().trim())) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if (!checkConditionService.checkUserbyUsername(userDTO.getUsername().trim())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
//        if (!checkConditionService.checkEmail(userDTO.getFullname())) {
//            throw new AppException(ErrorCode.INVALID_FULL_NAME);
//        }

    }

    @Override
    public void checkConditionsForAdmin(User_Admin_DTO userDTO) {
        if (!checkConditionService.checkEmail(userDTO.getEmail().trim())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
        if (!checkConditionService.checkName(userDTO.getUsername().trim())) {
            throw new AppException(ErrorCode.INVALID_NAME_FORMAT);
        }
        if (!userDTO.getPassword().equals(userDTO.getCheckPass().trim())) {
            throw new AppException(ErrorCode.NOT_MATCH_PASS);
        }
        if (!checkConditionService.checkUserbyEmail(userDTO.getEmail().trim())) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if (!checkConditionService.checkUserbyUsername(userDTO.getUsername().trim())) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
//        if (!checkConditionService.checkEmail(userDTO.getFullname())) {
//            throw new AppException(ErrorCode.INVALID_FULL_NAME);
//        }
    }


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
    public List<UserDTO> MultiFilterUser(String search, Integer roleId, Integer position_id) {
        List<User> userList = new ArrayList<>();
        if (search != null || roleId != null || position_id != null ) {
            userList = userRepository.MultiFilterUser(search, roleId, position_id);
        } else {
            userList = userRepository.findAll();
        }
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
    public UpdateProfileDTO ViewProfile() {
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user =userRepository.getUserByUsername(userDetails.getUsername());
        return modelMapper.map(user, UpdateProfileDTO.class);
    }

    @Transactional
    @Override
    public UserDTO UpdateProfile(UpdateProfileDTO updateProfileDTO){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername().trim();
        Optional<User> userOptional = userRepository.findByUsername(username);
        User user = userOptional.orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
//        if (!checkConditionService.checkAddress(updateProfileDTO.getAddress())) {
//            throw new AppException(ErrorCode.INVALID_FORMAT_ADDRESS);
//        }
        if (!checkConditionService.checkPhoneNumber(updateProfileDTO.getPhoneNumber().trim())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONE_NUMBER);
        }
        if (!checkConditionService.checkFullName(updateProfileDTO.getFullname().trim())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }
        if (!updateProfileDTO.getEmail().trim().equals(user.getEmail().trim()) &&
                userRepository.findByEmail(updateProfileDTO.getEmail().trim()).isPresent()) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if (!updateProfileDTO.getUsername().trim().equals(user.getUsername().trim()) &&
                userRepository.findByUsername(updateProfileDTO.getUsername().trim()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
//        if (!checkConditionService.checkEmail(updateProfileDTO.getFullname())) {
//            throw new AppException(ErrorCode.INVALID_FULL_NAME);
//        }

        user.setUsername(updateProfileDTO.getUsername().trim());
              user.getUserInfor().setFullname(updateProfileDTO.getFullname().trim());
        user.setEmail(updateProfileDTO.getEmail().trim());
        user.getUserInfor().setAddress(updateProfileDTO.getAddress());
        user.getUserInfor().setPhoneNumber(updateProfileDTO.getPhoneNumber().trim());
        user.getUserInfor().setBank_name(updateProfileDTO.getBank_name());
        user.getUserInfor().setBank_number(updateProfileDTO.getBank_number());
        user.getUserInfor().setCity_province(updateProfileDTO.getCity());
        user.getUserInfor().setDistrict(updateProfileDTO.getDistrict());
        user.getUserInfor().setWards(updateProfileDTO.getWards());
userRepository.save(user);
   //     entityManager.refresh(user); // Làm mới đối tượng
        return modelMapper.map(user, UserDTO.class);

    }
    @Transactional
    @Override
    public UserDTO EditUser(int id, EditUserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        if (!checkConditionService.checkPhoneNumber(userDTO.getPhoneNumber().trim())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_PHONE_NUMBER);
        }
        if (!checkConditionService.checkFullName(userDTO.getFullname().trim())) {
            throw new AppException(ErrorCode.INVALID_FORMAT_NAME);
        }

        if (!userDTO.getEmail().trim().equals(user.getEmail().trim()) &&
                userRepository.findByEmail(userDTO.getEmail().trim()).isPresent()) {
            throw new AppException(ErrorCode.GMAIL_EXISTED);
        }
        if (!userDTO.getPhoneNumber().trim().equals(user.getUserInfor().getPhoneNumber()) &&
                userRepository.findByPhone(userDTO.getPhoneNumber().trim()).isPresent()) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }

        if (!userDTO.getUsername().trim().equals(user.getUsername().trim()) &&
                userRepository.findByUsername(userDTO.getUsername().trim()).isPresent()) {
            throw new AppException(ErrorCode.USERNAME_EXISTED);
        }
        if (!checkConditionService.checkEmail(userDTO.getEmail().trim())) {
            throw new AppException(ErrorCode.WRONG_FORMAT_EMAIL);
        }
//        if(user.getRole().getRoleId()==4 && jobRepository.countJobsByUserId(id) >= 1) { //nếu sửa thằng employee thì nó phải chưa nhận job nào thì mới có quyền sửa role cho nó
//            throw new AppException(ErrorCode.NOT_EDIT_EMPLOYEE);
//        }
        // Tải rõ ràng UserInfor (Tải EAGER được ưu tiên trong trường hợp này)
        UserInfor userInfor = user.getUserInfor();
        entityManager.refresh(user); // Làm mới thực thể user trước khi sửa đổi
        // Thực hiện xác thực (như trong mã hiện tại của bạn)
        // Cập nhật trực tiếp các thuộc tính của user
        user.setUsername(userDTO.getUsername().trim());
        user.setEmail(userDTO.getEmail().trim());

        // Cập nhật trực tiếp các thuộc tính của userInfor
        userInfor.setPhoneNumber(userDTO.getPhoneNumber().trim());
        userInfor.setAddress(userDTO.getAddress());
        userInfor.setFullname(userDTO.getFullname());
        userInfor.setBank_number(userDTO.getBank_number());
        userInfor.setBank_name(userDTO.getBank_name());
        userInfor.setWards(userDTO.getWards());
        userInfor.setDistrict(userDTO.getDistrict());
        userInfor.setCity_province(userDTO.getCity_province());

        // Lấy và đặt Position, Status_User, và Role (giả sử các phương thức repository là chính xác)
        Position position = positionRepository.findById1(userDTO.getPosition_id());
        user.setPosition(position);
        Status_User statusUser = statusRepository.findById1(userDTO.getStatus_id());
        user.setStatus(statusUser);

        Role role = roleRepository.findByIdEdit(userDTO.getRole_id());

        user.setRole(role);


//        if (!checkConditionService.checkEmail(userDTO.getFullname())) {
//            throw new AppException(ErrorCode.INVALID_FULL_NAME);
//        }


        userRepository.save(user); // Điều này cũng sẽ lưu các thay đổi vào UserInfor liên kết

        return modelMapper.map(user, UserDTO.class);
    }


    @Override
    public List<User> getAllEmployee() {
        List<User> list = userRepository.findUsersWithPosition();
        if(list.isEmpty()){
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return list;
    }


    @Transactional
    //Đảm bảo tính toàn vẹn dữ liệu, nếu có lỗi thì tất cả các thao tác sẽ được rollback (hoàn tác)
    @Override
    public void DeleteUserById(int UserId) {
        User user = userRepository.findById(UserId).get();
        int info_id = user.getUserInfor().getInforId();
        informationUserRepository.deleteById(info_id);
        userRepository.DeleteById(UserId);
    }

    //Ban Account For Admin
    @Transactional
    @Override
  public  void changeStatusAccount(int id,int status_id){
        userRepository.editStatus(id,status_id);
    }

    @Override
    public List<UserDTO> FilterByStatus(int status_id) {
        List<User> userList = userRepository.FilterByStatus(status_id);
        if (userList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> FilterByRole(int roleId) {
        List<User> userList = userRepository.FilterByRole(roleId);
        if (userList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<UserDTO> FilterByPosition(int position_id) {
        List<User> userList = userRepository.FilterByPosition(position_id);
        if (userList.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND);
        }
        return userList.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }


}


