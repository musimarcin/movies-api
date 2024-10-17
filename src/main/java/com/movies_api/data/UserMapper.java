package com.movies_api.data;

import com.movies_api.data.DTO.UserDTO;
import com.movies_api.data.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {


    public UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRegistered());
    }
}
