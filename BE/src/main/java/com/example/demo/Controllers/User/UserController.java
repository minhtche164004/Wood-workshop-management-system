package com.example.demo.Controllers.User;

import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth1/user/")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @PutMapping("/UpdateProfile")
    public ApiResponse<?> UpdateProfile(@RequestBody UpdateProfileDTO updateProfileDTO){
        ApiResponse<UserDTO> apiResponse= new ApiResponse<>();
        apiResponse.setResult(userService.UpdateProfile(updateProfileDTO));
        return apiResponse;
    }

    @GetMapping("/CheckGetPrincipal")
    public ApiResponse<?> UpdateProfile(){
        ApiResponse<UserDetails> apiResponse= new ApiResponse<>();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        apiResponse.setResult(userDetails);
        return apiResponse;
    }

}
