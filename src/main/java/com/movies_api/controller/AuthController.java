package com.movies_api.controller;

import com.movies_api.data.DTO.CreateUserRequest;
import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.RoleRepo;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;


    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody CreateUserRequest request) {
        if (userRepo.existsByUsername(request.getUsername()))
            return new ResponseEntity<>("Username taken", HttpStatus.BAD_REQUEST);
        Role roles = roleRepo.findByName("USER").get();
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Collections.singletonList(roles));
        userRepo.save(user);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody CreateUserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("Logged in successfully", HttpStatus.OK);
    }
}
