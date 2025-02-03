package com.example.videostreamingapi.entity;

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
    private int impressions = 0;

    public VideoEngagement() {
    }

    public VideoEngagement(Video video, int views, int impressions) {
        this.video = video;
        this.views = views;
        this.impressions = impressions;
    }

    public void incrementViews() {
        this.views++;
    }

    public void incrementImpressions() {
        this.impressions++;
    }
}