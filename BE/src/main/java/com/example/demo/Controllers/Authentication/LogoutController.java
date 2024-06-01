package com.example.demo.Controllers.Authentication;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins="http://localhost:5173")
@AllArgsConstructor
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity registerUserAccount(HttpSession session) {
        session.removeAttribute("token");
        return ResponseEntity.ok("Logout successfully");
    }
}
