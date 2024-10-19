package com.movies_api.data.DTO;

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
}
