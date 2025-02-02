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
}