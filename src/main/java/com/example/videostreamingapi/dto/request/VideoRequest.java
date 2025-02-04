package com.example.videostreamingapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request payload for creating or updating a video")
public class VideoRequest {

    @Schema(description = "Title of the video", example = "Inception")
    private String title;

    @Schema(description = "Director of the video", example = "Christopher Nolan")
    private String director;

    @Schema(description = "Main actor in the video", example = "Leonardo DiCaprio")
    private String mainActor;

    @Schema(description = "Genre of the video", example = "Sci-Fi")
    private String genre;

    @Schema(description = "Running time in minutes", example = "148")
    private int runningTime;
}