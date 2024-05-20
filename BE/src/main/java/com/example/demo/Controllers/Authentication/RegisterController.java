package com.example.demo.Controllers.Authentication;


import com.example.demo.Dto.UserDTO;
import com.example.demo.Entity.User;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class RegisterController {

    @Autowired
    private UserService userService;

    //thêm cái @Valid vi toi bo cai validation vao trong UserDto
    //trả về object ApiResponse chứ ko phải uSER NỮA
    @PostMapping("/registration")
    ApiResponse<User> registerUserAccount(@RequestBody @Valid UserDTO userDto) {
        ApiResponse<User> apiResponse= new ApiResponse<>();

        apiResponse.setResult( userService.signup(userDto));
        return apiResponse;
     /*   userService.signup(userDto);
        return ResponseEntity.ok("Registration successful");*/
    }
}
