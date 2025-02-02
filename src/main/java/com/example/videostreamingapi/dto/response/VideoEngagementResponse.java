package com.example.videostreamingapi.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoEngagementResponse {
    private Long videoId;
    private int views;
    private int impressions;

    public VideoEngagementResponse(Long videoId, int views, int impressions) {
        this.videoId = videoId;
        this.views = views;
        this.impressions = impressions;
    }
}