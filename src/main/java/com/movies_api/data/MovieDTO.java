package com.movies_api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Instant addedWhen;
}
