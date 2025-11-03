package com.example.auth_service.authController;

import com.example.auth_service.authEntity.User;
import com.example.auth_service.authService.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        try {
            User newUser = authService.registerUser(
                    body.get("name"),
                    body.get("email"),
                    body.get("password"),
                    body.get("phone"),
                    body.get("role")
            );
            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "userId", newUser.getId(),
                    "email", newUser.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        try {
            String token = authService.login(body.get("email"), body.get("password"));
            return ResponseEntity.ok(Map.of("token", token));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ping")
    public String ping() {
        return "✅ Auth Service is running";
    }

    @GetMapping("/farmer/data")
    public String farmerData() {
        return "Farmer specific data";
    }

    @GetMapping("/manager/data")
    public String managerData() {
        return "Manager specific data";
    }
}
