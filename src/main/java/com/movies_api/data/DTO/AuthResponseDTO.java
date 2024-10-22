package com.movies_api.data.DTO;

import lombok.Data;

import java.util.List;

@Data

public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

}
