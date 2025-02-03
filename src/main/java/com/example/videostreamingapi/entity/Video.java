package com.example.videostreamingapi.entity;

import java.util.ArrayList;
import java.util.List;

import com.example.videostreamingapi.dto.request.VideoRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Getter
@Setter
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String director;
    private String mainActor;
    private String genre;
    private int runningTime;
    private boolean active = true;

    public Video() {
    }

    public Video(VideoRequest videoRequest) {
        this.title = videoRequest.getTitle();
        this.director = videoRequest.getDirector();
        this.mainActor = videoRequest.getMainActor();
        this.genre = videoRequest.getGenre();
        this.runningTime = videoRequest.getRunningTime();
        this.active = true;
    }

    public Video(String title, String director, String mainActor, String genre, int runningTime) {
        this.title = title;
        this.director = director;
        this.mainActor = mainActor;
        this.genre = genre;
        this.runningTime = runningTime;
        this.active = true;
    }

    public void updateMetadata(VideoRequest request) {
        this.title = request.getTitle();
        this.director = request.getDirector();
        this.mainActor = request.getMainActor();
        this.genre = request.getGenre();
        this.runningTime = request.getRunningTime();
    }

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoEngagement> engagements = new ArrayList<>();
}