package com.movies_api.data.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateMovieRequest {
    //this class is used instead of MovieDTO because request will need title and release year instead of all Movie properties
    //handling errors of missing fieldss
    @NotEmpty(message = "Fill in title")
    private String title;
    @NotNull(message = "Fill in release year")
    private Integer releaseYear;
}