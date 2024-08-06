package com.example.demo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.example.demo.Dto.UserDTO.ChangePassDTO;
import com.example.demo.Dto.UserDTO.RegisterDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Dto.UserDTO.User_Admin_DTO;
import com.example.demo.Entity.*;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Jwt.UserDetailsServiceImpl;
import com.example.demo.Repository.*;
import com.example.demo.Service.CheckConditionService;
import com.example.demo.Service.Impl.UserServiceImpl;
import com.example.demo.Service.JWTService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private JWTService jwtService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private Status_User_Repository statusRepository;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @Mock
    private InformationUserRepository informationUserRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CheckConditionService checkConditionService;
    @Mock
    private EntityManager entityManager;
    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private ChangePassDTO changePassDTO;

    private User user;
    private UserDTO userDTO;

    private UserDetails userDetails;

    private User_Admin_DTO userAdminDTO;

    private RegisterDTO registerDTO;

    @BeforeEach
    public void setUp() {
        registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("password");
        registerDTO.setEmail("testuser@example.com");
        registerDTO.setPhoneNumber("1234567890");
        registerDTO.setFullname("Test User");
        registerDTO.setAddress("123 Test St");
        registerDTO.setCity("Test City");
        registerDTO.setDistrict("Test District");
        registerDTO.setWards("Test Wards");

        userAdminDTO = new User_Admin_DTO();
        userAdminDTO.setUsername("adminUser");
        userAdminDTO.setPassword("adminPass");
        userAdminDTO.setEmail("123@gmail");
        userAdminDTO.setPhoneNumber("1312331");
        userAdminDTO.setFullname("123");
        userAdminDTO.setAddress("456 Admin Street");
        userAdminDTO.setCity("Admin City");
        userAdminDTO.setDistrict("Admin District");
        userAdminDTO.setWards("Admin Wards");
        userAdminDTO.setRole(1); // assume this is a valid role ID
        userAdminDTO.setPosition(2); // assume this is a valid position ID
        userAdminDTO.setBank_name("BankName");
        userAdminDTO.setBank_number("123456789");
//        changePassDTO = new ChangePassDTO("oldPass","oldPass","oldPass");
//        changePassDTO.setOld_pass("oldPass");
//        changePassDTO.setNew_pass("newPass");
//        changePassDTO.setCheck_pass("newPass");
//
//        // Set up User and UserDetails
//        user = new User();
//        user.setUsername("testuser");
//        user.setPassword("encodedOldPass");
//
//        userDetails = mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn("testuser");
//
//        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));
    }

    @Test
    public void testSignup() {
        // Arrange
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn(encodedPassword);
        when(roleRepository.findById(2)).thenReturn(new Role(2, "CUSTOMER", null));
        when(statusRepository.findById(2)).thenReturn(new Status_User(2, "KICHHOAT"));
        when(positionRepository.findById(4)).thenReturn(new Position(4, "Không đảm nhận vị trí"));

        UserInfor userInfor = new UserInfor(
                registerDTO.getPhoneNumber(),
                registerDTO.getFullname(),
                registerDTO.getAddress(),
                "",
                "",
                registerDTO.getCity(),
                registerDTO.getDistrict(),
                registerDTO.getWards(),
                1
        );
        when(informationUserRepository.save(any(UserInfor.class))).thenReturn(userInfor);

        User user = new User(
                0,
                registerDTO.getUsername(),
                encodedPassword,
                registerDTO.getEmail(),
                new Status_User(2, "KICHHOAT"),
                new Position(4, "Không đảm nhận vị trí"),
                Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()),
                new Role(2, "CUSTOMER", null),
                userInfor
        );
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.signup(registerDTO);

        // Assert
        assertEquals(registerDTO.getUsername(), result.getUsername());
        assertEquals(encodedPassword, result.getPassword());
        assertEquals(registerDTO.getEmail(), result.getEmail());
        verify(informationUserRepository, times(1)).save(any(UserInfor.class));
        verify(userRepository, times(1)).save(any(User.class));
    }
    @Test
    public void testSignupUserRepositoryThrowsException() {
        // Arrange
        String encodedPassword = "encodedPassword";
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn(encodedPassword);
        when(roleRepository.findById(2)).thenReturn(new Role(2, "CUSTOMER",null));
        when(statusRepository.findById(2)).thenReturn(new Status_User(2, "KICHHOAT"));
        when(positionRepository.findById(4)).thenReturn(new Position(4, "Không đảm nhận vị trí"));

        UserInfor userInfor = new UserInfor(
                registerDTO.getPhoneNumber(),
                registerDTO.getFullname(),
                registerDTO.getAddress(),
                "",
                "",
                registerDTO.getCity(),
                registerDTO.getDistrict(),
                registerDTO.getWards(),
                1
        );
        when(informationUserRepository.save(any(UserInfor.class))).thenReturn(userInfor);
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.signup(registerDTO));
        assertEquals("Database error", exception.getMessage());
        verify(informationUserRepository, times(1)).save(any(UserInfor.class));
        verify(userRepository, times(1)).save(any(User.class));
    }


    @Test
    public void testSignupRoleRepositoryReturnsNull() {
        // Arrange
        when(roleRepository.findById(2)).thenReturn(null);

        // Act & Assert
        User result = userService.signup(registerDTO);

        // Assert
        assertEquals(null,result);
    }


    @Test
    public void testSignupPositionRepositoryReturnsNull() {
        // Arrange
        when(positionRepository.findById(4)).thenReturn(null);

        // Act
        User result = userService.signup(registerDTO);

        // Assert
        assertEquals(null,result);
    }

    @Test
    public void testCreateAccountForAdminUsernameExisted() {
        // Arrange
        when(checkConditionService.checkUserbyUsername(userAdminDTO.getUsername())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(userAdminDTO));
        assertEquals(ErrorCode.USERNAME_EXISTED, exception.getErrorCode());
    }

    @Test
    public void testCreateAccountForAdminInvalidEmail() {
        // Arrange
        when(checkConditionService.checkUserbyUsername(userAdminDTO.getUsername())).thenReturn(true);
        when(checkConditionService.checkEmail(userAdminDTO.getEmail())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(userAdminDTO));
        assertEquals(ErrorCode.WRONG_FORMAT_EMAIL, exception.getErrorCode());
    }

    @Test
    public void testCreateAccountForAdminEmailExisted() {
        // Arrange
        when(checkConditionService.checkUserbyUsername(userAdminDTO.getUsername())).thenReturn(true);
        when(checkConditionService.checkEmail(userAdminDTO.getEmail())).thenReturn(true);
        when(checkConditionService.checkUserbyEmail(userAdminDTO.getEmail())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(userAdminDTO));
        assertEquals(ErrorCode.GMAIL_EXISTED, exception.getErrorCode());
    }

    @Test
    public void testCreateAccountForAdminPhoneExisted() {
        // Arrange
        when(checkConditionService.checkUserbyUsername(userAdminDTO.getUsername())).thenReturn(true);
        when(checkConditionService.checkEmail(userAdminDTO.getEmail())).thenReturn(true);
        when(checkConditionService.checkUserbyEmail(userAdminDTO.getEmail())).thenReturn(true);
        when(checkConditionService.checkPhone(userAdminDTO.getPhoneNumber())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(userAdminDTO));
        assertEquals(ErrorCode.PHONE_EXISTED, exception.getErrorCode());
    }

    @Test
    public void testCreateAccountForAdminInvalidPhoneNumber() {
        // Arrange
        when(checkConditionService.checkUserbyUsername(userAdminDTO.getUsername())).thenReturn(true);
        when(checkConditionService.checkEmail(userAdminDTO.getEmail())).thenReturn(true);
        when(checkConditionService.checkUserbyEmail(userAdminDTO.getEmail())).thenReturn(true);
        when(checkConditionService.checkPhone(userAdminDTO.getPhoneNumber())).thenReturn(true);
        when(checkConditionService.checkPhoneNumber(userAdminDTO.getPhoneNumber())).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(userAdminDTO));
        assertEquals(ErrorCode.INVALID_FORMAT_PHONE_NUMBER, exception.getErrorCode());
    }
//    @Test
//    public void testCreateAccountForAdminNullPhoneNumber() {
//        User_Admin_DTO admin1 = new User_Admin_DTO();
//        admin1.setUsername("testUsername");
//        admin1.setEmail("test@example.com");
//        admin1.setPhoneNumber(null); // Setting phone number to null
//
//        // Arrange
//        when(checkConditionService.checkUserbyUsername(admin1.getUsername())).thenReturn(true);
//        when(checkConditionService.checkEmail(admin1.getEmail())).thenReturn(true);
//        when(checkConditionService.checkUserbyEmail(admin1.getEmail())).thenReturn(true);
//
//        // Assuming that the service may be checking for an existing phone number
//        // Adjust the exception thrown by the mock to reflect this
//        when(checkConditionService.checkPhone(admin1.getPhoneNumber())).thenThrow(new IllegalArgumentException("Phone number must not be null"));
//
//        // Act & Assert
//        AppException exception = assertThrows(AppException.class, () -> userService.CreateAccountForAdmin(admin1));
//        assertEquals(ErrorCode.PHONE_EXISTED, exception.getErrorCode());
//    }
}
