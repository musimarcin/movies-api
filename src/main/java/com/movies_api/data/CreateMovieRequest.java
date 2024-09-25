package com.movies_api.data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMovieRequest {
    //this class is used instead of MovieDTO because request will need title and release year instead of all Movie properties
    @NotEmpty(message = "Fill in title")
    private String title;
    @NotNull(message = "Fill in release year")
    private Integer releaseYear;
}