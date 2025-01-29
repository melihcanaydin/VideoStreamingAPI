package com.example.videostreamingapi.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "video_engagement")
public class VideoEngagement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "video_id", nullable = false, unique = true)
    private Video video;

    private int views = 0;
    private int likes = 0;

    public void incrementViews() {
        this.views++;
    }

    public void incrementLikes() {
        this.likes++;
    }
}