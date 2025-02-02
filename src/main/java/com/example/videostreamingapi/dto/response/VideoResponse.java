package com.example.videostreamingapi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoResponse {
    private Long id;
    private String title;
    private String director;
    private String mainActor;
    private String genre;
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