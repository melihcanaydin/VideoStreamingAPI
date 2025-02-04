package com.example.videostreamingapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response object containing video details")
public class VideoResponse {

    @Schema(description = "Unique identifier of the video", example = "1")
    private Long id;

    @Schema(description = "Title of the video", example = "Inception")
    private String title;

    @Schema(description = "Director of the video", example = "Christopher Nolan")
    private String director;

    @Schema(description = "Main actor of the video", example = "Leonardo DiCaprio")
    private String mainActor;

    @Schema(description = "Genre of the video", example = "Sci-Fi")
    private String genre;

    @Schema(description = "Running time of the video in minutes", example = "148")
    private int runningTime;

    public VideoResponse(Long id, String title, String director, String mainActor, String genre, int runningTime) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.mainActor = mainActor;
        this.genre = genre;
        this.runningTime = runningTime;
    }

    public VideoResponse() {
    }
}