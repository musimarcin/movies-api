package com.movies_api.controller;

import com.movies_api.data.DTO.CreateUserRequest;
import com.movies_api.data.UserMapper;
import com.movies_api.security.JWTGenerator;
import com.movies_api.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
        if (userService.checkUser(request.getUsername())) {
            return new ResponseEntity<>("Username taken", HttpStatus.BAD_REQUEST);
        }
        if (request.getUsername().isEmpty() ||
                request.getPassword().isEmpty() ||
                request.getEmail().isEmpty()) {
            return new ResponseEntity<>("Fields missing", HttpStatus.BAD_REQUEST);
        }
        userService.createUser(request);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody CreateUserRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication);
            Cookie cookie = getCookie(token);
            response.addCookie(cookie);
            return new ResponseEntity<>("Logged in successfully", HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Credentials incorrect", HttpStatus.UNAUTHORIZED);
        }
    }

    private static Cookie getCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3500);
        cookie.setSecure(true);
        return cookie;
    }
}
