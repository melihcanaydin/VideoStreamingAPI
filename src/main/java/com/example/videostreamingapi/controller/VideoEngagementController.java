package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class VideoEngagementController {

    private final VideoEngagementService engagementService;

    public VideoEngagementController(VideoEngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping("/videos/{videoId}/engagement")
    public String getEngagementStats(@PathVariable Long videoId) {
        VideoEngagement engagement = engagementService.getEngagementStats(videoId);
        return "Views: " + engagement.getViews() + ", Impressions: " + engagement.getImpressions();
    }
}