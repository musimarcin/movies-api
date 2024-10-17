package com.movies_api.service;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.UserMapper;
import com.movies_api.data.entity.User;
import com.movies_api.data.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepo userRepo;
    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(String username, String password, String email) {
        String hashedPassword = passwordEncoder.encode(password);
        User user = new User(null, username, hashedPassword, email, Instant.now());
        User savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUser(String username, String password, String email) {
        User user = userRepo.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword()))
            return userMapper.toDTO(user);
        else return null;
    }
}
