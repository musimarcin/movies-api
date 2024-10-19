package com.movies_api.data;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.Movie;
import com.movies_api.data.entity.Role;
import com.movies_api.data.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDTO toDTO(UserEntity user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRegistered()
        );
    }

    public UserEntity toEntity(UserDTO userDTO, List<Role> roles) {
        return new UserEntity(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRegistered(),
                roles
        );
    }
}
