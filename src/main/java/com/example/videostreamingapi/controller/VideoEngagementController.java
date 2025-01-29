package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos/engagement")
public class VideoEngagementController {

    private final VideoEngagementService engagementService;

    public VideoEngagementController(VideoEngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @PostMapping("/{videoId}/view")
    public void addView(@PathVariable Long videoId) {
        engagementService.incrementViewCount(videoId);
    }

    @PostMapping("/{videoId}/like")
    public void addLike(@PathVariable Long videoId) {
        engagementService.incrementLikeCount(videoId);
    }

    @GetMapping("/{videoId}")
    public VideoEngagement getEngagementStats(@PathVariable Long videoId) {
        return engagementService.getEngagementStats(videoId);
    }
}