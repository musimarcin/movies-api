package com.movies_api.data;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.Movie;
import com.movies_api.data.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRegistered()
        );
    }

    public User toEntity(UserDTO userDTO, List<Movie> movies) {
        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getEmail(),
                userDTO.getRegistered()
        );
    }
}
