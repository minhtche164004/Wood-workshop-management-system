package com.example.demo.Controllers.Admin;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Dto.UserUpdateDTO;
import com.example.demo.Entity.User;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
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
    private UserRepository userRepository;

    @GetMapping("/GetAllUser")
    public ApiResponse<?> getAllUser(){
        ApiResponse<List> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.GetAllUser());
        return apiResponse;

    }
    @GetMapping("/SearchUserByNameorAddress")
    public ApiResponse<?> SearchUserByName(@RequestParam(value = "query", required = false) String query){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FindByUsernameOrAddress(query));
        return  apiResponse;
    }

    @GetMapping("GetById1")
    public  ApiResponse<?> getUserById1(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FindbyId(user_id));
        return apiResponse;
    }
    @GetMapping("GetById2")
    public  ApiResponse<?> getUserById2(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<UserUpdateDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.GetUserById(user_id));
        return apiResponse;
    }
    @GetMapping("GetById3")
    public  ApiResponse<?> getUserById3(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FindbyId1(user_id));
        return apiResponse;
    }

    @DeleteMapping("DeleteUserById")
    public  ApiResponse<?> DeleteUserById(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.DeleteUserById(user_id);
        apiResponse.setResult("Xoa User thanh cong");
        return apiResponse;
    }

}
