package com.movies_api.data.DTO;

import com.movies_api.data.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Instant addedWhen;
    private Long userId;
}
