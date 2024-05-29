package com.example.demo.Controllers.Authentication;

import com.example.demo.Dto.UserDTO;
import com.example.demo.Response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity registerUserAccount(HttpSession session) {
        session.removeAttribute("token");
        return ResponseEntity.ok("Logout successfully");
    }
}
