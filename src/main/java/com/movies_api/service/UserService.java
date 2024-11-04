package com.movies_api.service;

import com.movies_api.data.DTO.CreateUserRequest;
import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.RoleRepo;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.data.UserMapper;
import com.movies_api.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(CreateUserRequest request) {
        Role roles = roleRepo.findByName("USER").get();
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Collections.singletonList(roles));
        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    public boolean checkUser(String username) {
        return userRepo.existsByUsername(username);
    }

    public UserDTO getUser(String username) {
        return userMapper.toDTO(userRepo.findByUsername(username));
    }

    public boolean checkLoggedIn() {
        return (SecurityUtil.getSessionUser() != null);
    }

    @Transactional
    public boolean deleteUser(String username) {
        UserEntity user = userRepo.findByUsername(username);
        if (user != null) {
            userRepo.delete(user);
            return true;
        } else return false;
    }

    @Transactional
    public boolean changeUsername(String oldUsername, String newUsername) {
        if (userRepo.findByUsername(oldUsername) != null) {
            int changes = userRepo.updateUsername(oldUsername, newUsername);
            return changes > 0;
        } else return false;
    }

    @Transactional
    public boolean changePassword(String username, String password) {
        if (userRepo.findByUsername(username) != null) {
            int changes = userRepo.updatePassword(username, password);
            return changes > 0;
        } else return false;
    }

    @Transactional
    public boolean changeEmail(String username, String email) {
        if (userRepo.findByUsername(username) != null) {
            int changes = userRepo.updateEmail(username, email);
            return changes > 0;
        } else return false;
    }

}
