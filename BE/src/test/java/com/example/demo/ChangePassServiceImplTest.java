package com.example.demo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dto.UserDTO.ChangePassDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Impl.UserServiceImpl;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ChangePassServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private ChangePassDTO changePassDTO;
    private User user;
    private UserDetails userDetails;
    private SecurityContext securityContext;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        // Initialize ChangePassDTO with test data
        changePassDTO = new ChangePassDTO(null,null,null);
        changePassDTO.setOld_pass("oldPass");
        changePassDTO.setNew_pass("newPass");
        changePassDTO.setCheck_pass("newPass");

        // Initialize User and UserDetails
        user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedOldPass");

        userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        // Set up Authentication and SecurityContext
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the behavior of the repository and encoder
        when(userRepository.getUserByUsername("testuser")).thenReturn(user);
    }

    @Test
    public void testChangePassSuccess() {
        // Arrange
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        // Act
        userService.changePass(changePassDTO);

        // Assert
        verify(userRepository).updatePassword(user.getEmail(), "encodedNewPass");
    }

    @Test
    public void testChangePassIncorrectOldPassword() {
        // Arrange
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(false);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.changePass(changePassDTO));
        assertEquals(ErrorCode.WRONG_PASS, exception.getErrorCode());
    }

    @Test
    public void testChangePassNewPasswordsDoNotMatch() {
        // Arrange
        changePassDTO.setCheck_pass("differentPass");
        when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.changePass(changePassDTO));
        assertEquals(ErrorCode.NOT_MATCH_PASS, exception.getErrorCode());
    }
}
