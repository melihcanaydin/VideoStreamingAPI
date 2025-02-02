package com.example.videostreamingapi.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Director is required")
    private String director;

    @NotBlank(message = "Main actor is required")
    private String mainActor;

    @NotBlank(message = "Genre is required")
    private String genre;

    @Min(value = 1, message = "Running time must be at least 1 minute")
    private int runningTime;
}