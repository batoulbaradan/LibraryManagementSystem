package com.example.Library.service.basic;


import com.example.Library.classes.dto.UserDTO;
import com.example.Library.classes.mapper.UserMapper;
import com.example.Library.exception.DuplicateUsernameException;
import com.example.Library.exception.InvalidCredentialsException;
import com.example.Library.model.basic.User;
import com.example.Library.repository.basic.UserRepository;
import com.example.Library.jwt.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private UserMapper userMapper;
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtUtil.generateToken(username);
        } catch (Exception e) {
            throw new InvalidCredentialsException("Invalid credentials provided");
        }
    }

    @Transactional
    public UserDTO register(String username, String password) {
        boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new DuplicateUsernameException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");

        try {
            userRepository.save(user);
            return userMapper.toDTO(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Failed to save user: " + ex.getRootCause().getMessage());
        }catch (Exception ex) {
            throw new RuntimeException("An unexpected error occurred while saving the user: " + ex.getMessage());
        }
    }
}