package com.example.demo;

import com.example.demo.Entity.Products;
import com.example.demo.Entity.User;
import com.example.demo.Entity.WishList;
import com.example.demo.Exception.AppException;
import com.example.demo.Exception.ErrorCode;
import com.example.demo.Repository.ProductRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.WishListRepository;
import com.example.demo.Service.Impl.WishListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class WishListServiceImplTest {

    @InjectMocks
    private WishListServiceImpl wishListServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WishListRepository wishListRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking SecurityContext and Authentication
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testAddWhiteList_Success() {
        // Setup
        User user = new User();
        user.setUserId(1);

        Products product = new Products();
        product.setProductId(1);

        WishList wishList = new WishList();
        wishList.setProduct(product);
        wishList.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyInt())).thenReturn(product);
        when(wishListRepository.findByUserID(anyInt())).thenReturn(new ArrayList<>());

        when(wishListRepository.save(any(WishList.class))).thenReturn(wishList);

        // Test
        WishList result = wishListServiceImpl.AddWhiteList(1);

        // Verify
        assertNotNull(result);
        assertEquals(product, result.getProduct());
        assertEquals(user, result.getUser());
        verify(wishListRepository).save(any(WishList.class));
    }

    @Test
    void testAddWhiteList_ExistingWishList() {
        // Setup
        User user = new User();
        user.setUserId(1);

        Products product = new Products();
        product.setProductId(1);

        WishList existingWishList = new WishList();
        existingWishList.setProduct(product);
        existingWishList.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(productRepository.findById(anyInt())).thenReturn(product);
        when(wishListRepository.findByUserID(anyInt())).thenReturn(List.of(existingWishList));

        // Test
        AppException thrown = assertThrows(AppException.class, () -> {
            wishListServiceImpl.AddWhiteList(1);
        });

        // Verify
        assertEquals(ErrorCode.EXISTED_WISHLIST, thrown.getErrorCode());
    }

    @Test
    void testDeleteWhiteList_Success() {
        // Setup
        doNothing().when(wishListRepository).deleteById(anyInt());

        // Test
        assertDoesNotThrow(() -> wishListServiceImpl.DeleteWhiteList(1));

        // Verify
        verify(wishListRepository).deleteById(anyInt());
    }

    @Test
    void testViewWhiteList_Success() {
        // Setup
        User user = new User();
        user.setUserId(1);

        WishList wishList = new WishList();
        wishList.setUser(user);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(wishListRepository.findByUserID(anyInt())).thenReturn(List.of(wishList));

        // Test
        List<WishList> result = wishListServiceImpl.ViewWhiteList();

        // Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void testViewWhiteList_EmptyList() {
        // Setup
        User user = new User();
        user.setUserId(1);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(wishListRepository.findByUserID(anyInt())).thenReturn(new ArrayList<>());

        // Test
        AppException thrown = assertThrows(AppException.class, () -> {
            wishListServiceImpl.ViewWhiteList();
        });

        // Verify
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }


    @Test
    void testAddWhiteList_UserNotFound() {
        // Setup
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Test
        AppException thrown = assertThrows(AppException.class, () -> {
            wishListServiceImpl.AddWhiteList(1);
        });

        // Verify
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }
    @Test
    void testDeleteWhiteList_WishListNotFound() {
        // Setup
        // Mock the deleteById method to simulate no exception thrown (since item may not be found)
        doNothing().when(wishListRepository).deleteById(anyInt());

        // Test
        assertDoesNotThrow(() -> wishListServiceImpl.DeleteWhiteList(999));

        // Verify
        verify(wishListRepository).deleteById(anyInt());
    }


    @Test
    void testViewWhiteList_UserNotFound() {
        // Setup
        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Test
        AppException thrown = assertThrows(AppException.class, () -> {
            wishListServiceImpl.ViewWhiteList();
        });

        // Verify
        assertEquals(ErrorCode.NOT_FOUND, thrown.getErrorCode());
    }

    @Test
    void testViewWhiteList_WishListRetrievalFailure() {
        // Setup
        User user = new User();
        user.setUserId(1);

        when(userDetails.getUsername()).thenReturn("testUser");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(wishListRepository.findByUserID(anyInt())).thenThrow(new RuntimeException("Database error"));

        // Test
        Exception thrown = assertThrows(RuntimeException.class, () -> {
            wishListServiceImpl.ViewWhiteList();
        });

        // Verify
        assertEquals("Database error", thrown.getMessage());
    }

}