package com.example.demo.Controllers.Admin;

import com.example.demo.Config.RedisConfig;
import com.example.demo.Dto.UserDTO.*;
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
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;

import java.lang.reflect.Type;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Status_User_Repository statusUserRepository;
    private static final JedisPooled jedis = RedisConfig.getRedisInstance();

    //    @GetMapping("/GetAllPositionName")
//    public ApiResponse<?> GetAllPositionName(){
//        ApiResponse<List> apiResponse= new ApiResponse<>();
//        apiResponse.setResult(positionService.getListNamePosition());
//        return apiResponse;
//    }
    @GetMapping("/GetAllPosition")
    public ApiResponse<?> GetAllPosition() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_positions";
        List<Position> positions;
        String cachedData = jedis.get(cacheKey);
        if (cachedData != null) {
            Type type = new TypeToken<List<Position>>() {
            }.getType();
            positions = new Gson().fromJson(cachedData, type);
        } else {
            positions = positionService.getListPosition();
            String jsonData = new Gson().toJson(positions);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 3000);
        }
        apiResponse.setResult(positions);
        return apiResponse;
    }

    @GetMapping("/GetAllUser")
    public ApiResponse<?> getAllUser() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        String cacheKey = "all_users";
        //jedis.del(cacheKey);
        List<UserDTO> users;
        String cachedData = jedis.get(cacheKey);
        if (cachedData != null) {
            Type type = new TypeToken<List<UserDTO>>() {
            }.getType();
            users = new Gson().fromJson(cachedData, type);
        } else {
            users = userService.GetAllUser();
            String jsonData = new Gson().toJson(users);
            jedis.set(cacheKey, jsonData);
            jedis.expire(cacheKey, 3000);
        }
        apiResponse.setResult(users);
        return apiResponse;
    }


    @GetMapping("/GetAllRole")
    public ApiResponse<?> GetAllRole() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(roleRepository.findAll());
        return apiResponse;
    }
    @GetMapping("/GetStatusUser")
    public ApiResponse<?> GetStatusUser() {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(statusUserRepository.getAllStatusNames());
        return apiResponse;
    }

    @GetMapping("/SearchUserByNameorAddress")
    public ApiResponse<?> SearchUserByName(@RequestParam(value = "query", required = false) String query) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        // redis cache
        String cacheKey = "search_User_By_Name_Or_Address";
        List<UserDTO> users;
        String cachedData = jedis.hget(cacheKey, query);
        if (cachedData != null) {
            Type type = new TypeToken<List<UserDTO>>() {
            }.getType();
            users = new Gson().fromJson(cachedData, type);
        } else {
            users = userService.FindByUsernameOrAddress(query);
            String jsonData = new Gson().toJson(users);
            jedis.hset(cacheKey, query, jsonData);
            jedis.expire(cacheKey, 60);
        }
        // redis cache
        apiResponse.setResult(users);
        return apiResponse;
    }

    @GetMapping("/FilterByStatus")
    public ApiResponse<?> FilterByStatus(@RequestParam(value = "query", required = false) int query) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FilterByStatus(query));
        return apiResponse;
    }

    @GetMapping("/FilterByRole")
    public ApiResponse<?> FilterByRole(@RequestParam(value = "query", required = false) int query) {
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.FilterByRole(query));
        return apiResponse;
    }
    @GetMapping("/getAllEmployee")
    public ApiResponse<?> getAllEmployee(){
        ApiResponse<List> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getAllEmployee());
        return  apiResponse;
    }

    @GetMapping("GetById")
    public ApiResponse<?> getUserById1(@RequestParam(value = "user_id", required = false) int user_id) {
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
    public ApiResponse<?> DeleteUserById(@RequestParam(value = "user_id", required = false) int user_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.DeleteUserById(user_id);
        jedis.del("all_users");
        jedis.del("search_User_By_Name_Or_Address");
        apiResponse.setResult("Xoa User thanh cong");
        return apiResponse;
    }

    @PostMapping("AddNewAccount")
    public ApiResponse<?> AddNewAccount(@RequestBody User_Admin_DTO registerDTO) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        userService.checkConditionsForAdmin(registerDTO);
        apiResponse.setResult(userService.CreateAccountForAdmin(registerDTO));
        jedis.del("all_users");
        jedis.del("search_User_By_Name_Or_Address");
        return apiResponse;
    }

    @PutMapping("EditUser")
    public ApiResponse<?> EditUser(@RequestParam(value = "user_id", required = false) int user_id, @RequestBody EditUserDTO userDTO) {
        ApiResponse<UserDTO> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.EditUser(user_id, userDTO));
        jedis.del("all_users");
        jedis.del("search_User_By_Name_Or_Address");
        return apiResponse;
    }

    @PostMapping("ChangeStatusAccount")
    public ApiResponse<?> ChangeStatusAccount(@RequestParam(value = "user_id") int user_id,
                                              @RequestParam(value = "status_id") int status_id) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        userService.changeStatusAccount(user_id, status_id);
        jedis.del("all_users");
        jedis.del("search_User_By_Name_Or_Address");
        apiResponse.setResult("Sửa Status thành công");
        return apiResponse;
    }
}
