package com.example.videostreamingapi.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response object containing engagement statistics of a video")
public class VideoEngagementResponse {

    @Schema(description = "Unique identifier of the video", example = "1")
    private Long videoId;

    @Schema(description = "Total number of views", example = "1500")
    private int views;

    @Schema(description = "Total number of impressions", example = "3000")
    private int impressions;

    public VideoEngagementResponse(Long videoId, int views, int impressions) {
        this.videoId = videoId;
        this.views = views;
        this.impressions = impressions;
    }

    public VideoEngagementResponse() {
    }
}