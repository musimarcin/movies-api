package com.movies_api.service;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.UserEntity;
import com.movies_api.data.repository.UserRepo;
import com.movies_api.data.UserMapper;
import com.movies_api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = userMapper.toEntity(userDTO, new ArrayList<>());
        UserEntity savedUser = userRepo.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserEntity getUser(String username) {
        return userRepo.findByUsername(username);
    }

}
