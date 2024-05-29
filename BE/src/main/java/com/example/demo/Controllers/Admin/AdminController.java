package com.example.demo.Controllers.Admin;

import com.example.demo.Entity.User;
import com.example.demo.Jwt.JwtAuthenticationResponse;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/SearchUser")
    public ApiResponse<?> SearchUserByName(@RequestParam(value = "query", required = true) String query){
        ApiResponse<List> apiResponse = new ApiResponse<>();
       // apiResponse.setResult(userRepository.findByUsername(query));
        apiResponse.setResult(userRepository.findByUsernameOrAddress(query));
        return  apiResponse;
    }

    @GetMapping("GetById/{ID}")
    public ApiResponse<?>  GetUserByIdUsingPathVariable(@PathVariable int ID) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userRepository.filterById(ID));
        return apiResponse;
    }

//    @GetMapping("/GetById")
//    public ResponseEntity<User> GetUserByIdUsingParam(@RequestParam(value = "ID", required = true) int ID) {
//        return ResponseEntity.ok(userReponsitory.findById(ID));
//    }


}
