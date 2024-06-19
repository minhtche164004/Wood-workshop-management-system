package com.example.demo.Controllers.Admin;

import com.example.demo.Dto.UserDTO.RegisterDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Dto.UserDTO.UserUpdateDTO;
import com.example.demo.Dto.UserDTO.User_Admin_DTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.PositionService;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/admin/")
@AllArgsConstructor
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private RoleRepository roleRepository;

//    @GetMapping("/GetAllPositionName")
//    public ApiResponse<?> GetAllPositionName(){
//        ApiResponse<List> apiResponse= new ApiResponse<>();
//        apiResponse.setResult(positionService.getListNamePosition());
//        return apiResponse;
//    }
    @GetMapping("/GetAllPosition")
    public ApiResponse<?> GetAllPosition(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(positionService.getListPosition());
        return apiResponse;
    }

    @GetMapping("/GetAllUser")
    public ApiResponse<?> getAllUser(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.GetAllUser());
        return apiResponse;
    }
    @GetMapping("/GetAllRole")
    public ApiResponse<?> GetAllRole(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(roleRepository.findAll());
        return apiResponse;
    }
    @GetMapping("/SearchUserByNameorAddress")
    public ApiResponse<?> SearchUserByName(@RequestParam(value = "query", required = false) String query){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FindByUsernameOrAddress(query));
        return  apiResponse;
    }
    @GetMapping("/FilterByStatus")
    public ApiResponse<?> FilterByStatus(@RequestParam(value = "query", required = false) int query){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FilterByStatus(query));
        return  apiResponse;
    }
    @GetMapping("/FilterByRole")
    public ApiResponse<?> FilterByRole(@RequestParam(value = "query", required = false) int query){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FilterByRole(query));
        return  apiResponse;
    }

    @GetMapping("GetById")
    public  ApiResponse<?> getUserById1(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FindbyId(user_id));
        return apiResponse;
    }

//    @GetMapping("ViewProfile")
//    public  ApiResponse<?> ViewProfile(@RequestParam(value = "user_id", required = false) int user_id) {
//        ApiResponse<UserUpdateDTO> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(userService.ViewProfile(user_id));
//        return apiResponse;
//    }

    @DeleteMapping("DeleteUserById")
    public  ApiResponse<?> DeleteUserById(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.DeleteUserById(user_id);
        apiResponse.setResult("Xoa User thanh cong");
        return apiResponse;
    }

    @PostMapping("AddNewAccount")
    public ApiResponse<?> AddNewAccount(@RequestBody User_Admin_DTO registerDTO){
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.checkConditionsForAdmin(registerDTO);
        apiResponse.setResult(userService.CreateAccountForAdmin(registerDTO));
        return apiResponse;
    }
    @PutMapping("EditUser")
    public ApiResponse<?> EditUser(@RequestParam(value = "user_id", required = false) int user_id,@RequestBody UserDTO userDTO){
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.EditUser(user_id,userDTO));
        return apiResponse;
    }

    @PostMapping("ChangeStatusAccount")
    public ApiResponse<?> ChangeStatusAccount(@RequestParam(value = "user_id") int user_id,
                                              @RequestParam(value = "status_id") int status_id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.changeStatusAccount(user_id,status_id);
        apiResponse.setResult("Sửa Status thành công");
        return apiResponse;
    }
}
