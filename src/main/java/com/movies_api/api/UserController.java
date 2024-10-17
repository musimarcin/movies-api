package com.movies_api.api;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO registerUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.createUser(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail()
        );
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO user = userService.getUser(
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail()
        );
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return user;
    }
}
