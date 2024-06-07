package com.example.demo.Controllers.User;

import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/user/")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/UpdateProfile")
    public ApiResponse<?> UpdateProfile(@RequestParam(value = "user_id", required = false) int user_id, @RequestBody UpdateProfileDTO updateProfileDTO){
        ApiResponse<UserDTO> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.EditUser(user_id,updateProfileDTO));
        return apiResponse;
    }

}
