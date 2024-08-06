package com.example.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.InformationUserRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class UserServiceFilterImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private InformationUserRepository informationUserRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private UpdateProfileDTO updateProfileDTO;

    @Test
    public void testFindbyIdSuccess() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        UserDTO result = userService.FindbyId(1);

        // Assert
        assertNotNull(result);
        assertEquals(userDTO.getUsername(), result.getUsername());
    }

    @Test
    public void testFindbyIdNotFound() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.FindbyId(1));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testFindByUsernameOrAddressSuccess() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");

        List<User> users = List.of(user);
        when(userRepository.findByUsernameOrAddress("key")).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        List<UserDTO> result = userService.FindByUsernameOrAddress("key");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testFindByUsernameOrAddressEmptyList() {
        // Arrange
        when(userRepository.findByUsernameOrAddress("key")).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.FindByUsernameOrAddress("key"));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }


//    @Test
//    public void testViewProfile() {
//        // Arrange
//        User user = new User();
//        user.setUserId(1);
//        user.setUsername("testuser");
//
//        UpdateProfileDTO updateProfileDTO = new UpdateProfileDTO();
//        updateProfileDTO.setUsername("testuser");
//
//        UserDetails userDetails = mock(UserDetails.class);
//        when(userDetails.getUsername()).thenReturn("testuser");
//        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));
//
//        when(userRepository.getUserByUsername("testuser")).thenReturn(user);
//        when(modelMapper.map(user, UpdateProfileDTO.class)).thenReturn(updateProfileDTO);
//
//        // Act
//        UpdateProfileDTO result = userService.ViewProfile();
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(updateProfileDTO.getUsername(), result.getUsername());
//    }

    @Test
    public void testViewProfile() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken(userDetails, null));

        when(userRepository.getUserByUsername("testuser")).thenReturn(user);

        // Act
        UpdateProfileDTO result = userService.ViewProfile();

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).getUserByUsername("testuser");
    }




    @Test
    public void testChangeStatusAccount() {
        // Arrange
        int userId = 1;
        int statusId = 2;

        // Act
        userService.changeStatusAccount(userId, statusId);

        // Assert
        verify(userRepository).editStatus(userId, statusId);
    }

    @Test
    public void testFilterByStatusSuccess() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");

        List<User> users = List.of(user);
        when(userRepository.FilterByStatus(1)).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        List<UserDTO> result = userService.FilterByStatus(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testFilterByStatusNotFound() {
        // Arrange
        when(userRepository.FilterByStatus(1)).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.FilterByStatus(1));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testFilterByRoleSuccess() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");

        List<User> users = List.of(user);
        when(userRepository.FilterByRole(1)).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        List<UserDTO> result = userService.FilterByRole(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testFilterByRoleNotFound() {
        // Arrange
        when(userRepository.FilterByRole(1)).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.FilterByRole(1));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testFilterByPositionSuccess() {
        // Arrange
        User user = new User();
        user.setUserId(1);
        user.setUsername("testuser");

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1);
        userDTO.setUsername("testuser");

        List<User> users = List.of(user);
        when(userRepository.FilterByPosition(1)).thenReturn(users);
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        // Act
        List<UserDTO> result = userService.FilterByPosition(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userDTO.getUsername(), result.get(0).getUsername());
    }

    @Test
    public void testFilterByPositionNotFound() {
        // Arrange
        when(userRepository.FilterByPosition(1)).thenReturn(new ArrayList<>());

        // Act & Assert
        AppException exception = assertThrows(AppException.class, () -> userService.FilterByPosition(1));
        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCode());
    }

}