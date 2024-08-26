package com.example.demo.Controller;

import com.example.demo.Dto.UserDTO.EditUserDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Dto.UserDTO.User_Admin_DTO;
import com.example.demo.Entity.Position;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.Status_User_Repository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.PositionService;
import com.example.demo.Service.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import redis.clients.jedis.JedisPooled;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PositionService positionService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private Status_User_Repository statusUserRepository;

    @MockBean
    private JedisPooled jedis;

    private Gson gson = new Gson();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetAllUser() throws Exception {
        List<UserDTO> users = Collections.singletonList(new UserDTO("john.doe@example.com", "John Doe", "1234567890", "123 Street", "John Doe", "BankName", "123456"));

        when(userService.GetAllUser()).thenReturn(users);
        when(jedis.get(anyString())).thenReturn(null);

        mockMvc.perform(get("/api/auth/admin/GetAllUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result[0].username").value("John Doe"))
                .andExpect(jsonPath("$.result[0].email").value("john.doe@example.com"));
    }

    @Test
    void testGetAllRole() throws Exception {
        List<Role> roles = Collections.singletonList(new Role());

        when(roleRepository.findAll()).thenReturn(roles);

        mockMvc.perform(get("/api/auth/admin/GetAllRole"))
                .andExpect(status().isOk());
    }


    @Test
    void testSearchUserByName() throws Exception {
        List<UserDTO> users = Collections.singletonList(new UserDTO("john.doe@example.com", "John Doe", "1234567890", "123 Street", "John Doe", "BankName", "123456"));

        when(userService.FindByUsernameOrAddress(anyString())).thenReturn(users);
        when(jedis.hget(anyString(), anyString())).thenReturn(null);

        mockMvc.perform(get("/api/auth/admin/SearchUserByNameorAddress").param("query", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").exists())
                .andExpect(jsonPath("$.result[0].username").value("John Doe"))
                .andExpect(jsonPath("$.result[0].email").value("john.doe@example.com"));
    }

    @Test
    void testFilterByStatus() throws Exception {
        List<UserDTO> users = Collections.singletonList(new UserDTO("john.doe@example.com", "John Doe", "1234567890", "123 Street", "John Doe", "BankName", "123456"));

        when(userService.FilterByStatus(2)).thenReturn(users);

        mockMvc.perform(get("/api/auth/admin/FilterByStatus").param("query", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/auth/admin/DeleteUserById").param("user_id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Xoa User thanh cong"));
    }

    @Test
    void testAddNewAccount() throws Exception {
        User_Admin_DTO userAdminDTO = new User_Admin_DTO(
                "john.doe@example.com", // email
                "John Doe", // username
                "password", // password
                "password", // checkPass
                "1234567890", // phoneNumber
                "123 Street", // address
                "John Doe", // fullname
                1, // status
                1, // position
                1, // role
                "BankName", // bank_name
                "123456", // bank_number
                "City", // city
                "District", // district
                "Wards" // wards
        );

        mockMvc.perform(put("/api/auth/admin/AddNewAccount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userAdminDTO)))
                .andExpect(status().isOk());
    }
    @Test
    void testEditUser() throws Exception {
        EditUserDTO editUserDTO = new EditUserDTO("new.email@example.com", "New Name", "1234567890", "123 Street", "New Name", "BankName", "123456");

        mockMvc.perform(put("/api/auth/admin/EditUser")
                        .param("user_id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(editUserDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void testChangeStatusAccount() throws Exception {
        mockMvc.perform(post("/api/auth/admin/ChangeStatusAccount")
                        .param("user_id", "1")
                        .param("status_id", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value("Sửa Status thành công"));
    }

}