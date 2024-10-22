package com.movies_api.controller;

import com.movies_api.data.DTO.AuthResponseDTO;
import com.movies_api.data.DTO.CreateUserRequest;
import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.UserMapper;
import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.RoleRepo;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.security.CustomUserDetailService;
import com.movies_api.security.JWTGenerator;
import com.movies_api.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth/")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final UserService userService;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> registerUser(@RequestBody CreateUserRequest request) {
        if (userService.checkUser(request.getUsername()))
            return new ResponseEntity<>("Username taken", HttpStatus.BAD_REQUEST);
        if (request.getUsername().isEmpty() ||
                request.getPassword().isEmpty() ||
                request.getEmail().isEmpty()) {
            return new ResponseEntity<>("Fields missing", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(request);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> loginUser(@RequestBody CreateUserRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            cookie.setSecure(true);
            response.addCookie(cookie);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserDTO userDTO = userService.getUser(request.getUsername());
            AuthResponseDTO authResponseDTO = new AuthResponseDTO();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority()).collect(Collectors.toList());
            authResponseDTO.setId(userDTO.getId());
            authResponseDTO.setUsername(userDTO.getUsername());
            authResponseDTO.setEmail(userDTO.getEmail());
            authResponseDTO.setRoles(roles);
            authResponseDTO.setAccessToken(token);
            return new ResponseEntity<>(authResponseDTO, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new AuthResponseDTO(), HttpStatus.UNAUTHORIZED);
        }
    }
}
