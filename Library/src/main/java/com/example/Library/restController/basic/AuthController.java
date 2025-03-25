package com.example.Library.restController.basic;
import com.example.Library.classes.dto.UserDTO;
import com.example.Library.classes.response.ApiResponse;
import com.example.Library.service.basic.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody UserDTO userDTO) {
        String token = authService.login(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.ok(new ApiResponse<>(true, "User logged in successfully", token));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody UserDTO userDTO) {
        UserDTO registeredUser = authService.register(userDTO.getUsername(), userDTO.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User registered successfully", registeredUser));
    }
}



