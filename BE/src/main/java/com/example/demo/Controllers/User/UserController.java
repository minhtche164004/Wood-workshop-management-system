package com.example.demo.Controllers.User;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.UserDTO.ChangePassDTO;
import com.example.demo.Dto.UserDTO.UpdateProfileDTO;
import com.example.demo.Dto.UserDTO.UserDTO;
import com.example.demo.Entity.Products;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Response.ApiResponse;
import com.example.demo.Service.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisPooled;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/api/auth/user/")
@AllArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();

    @PutMapping("/UpdateProfile")
    public ApiResponse<?> UpdateProfile(@RequestBody UpdateProfileDTO updateProfileDTO){
        ApiResponse<UserDTO> apiResponse= new ApiResponse<>();
        jedis.del("UserProfile");
        apiResponse.setResult(userService.UpdateProfile(updateProfileDTO));
        return apiResponse;
    }

    @PutMapping("/ChangePass")
    public ApiResponse<?> ChangePass(@RequestBody ChangePassDTO changePassDTO){
        ApiResponse<String> apiResponse= new ApiResponse<>();
        userService.changePass(changePassDTO);
        apiResponse.setResult("Đổi mật khẩu thành công");
        return apiResponse;
    }

//View Profile
    @GetMapping("/ViewProfile")
    public ApiResponse<?> ViewProfile(){
        ApiResponse<UpdateProfileDTO> apiResponse= new ApiResponse<>();
//        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String cacheKey = "UserProfile";
        UpdateProfileDTO users;
        String cachedData = jedis.hget(cacheKey, userDetails.getUsername());
        Gson gson = new GsonBuilder().setDateFormat("MMM dd, yyyy").create();
        if (cachedData != null) {
            Type type = new TypeToken<UpdateProfileDTO>() {
            }.getType();
            users = gson.fromJson(cachedData, type);
        } else {
            users = userService.ViewProfile();
            String jsonData = gson.toJson(users);
            jedis.hset(cacheKey, userDetails.getUsername(), jsonData);
            jedis.expire(cacheKey, 3000);
        }
        apiResponse.setResult(users);
        return apiResponse;
    }

}
