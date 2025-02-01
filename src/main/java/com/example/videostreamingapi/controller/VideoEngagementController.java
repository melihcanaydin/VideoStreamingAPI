package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos/{videoId}/engagement")
public class VideoEngagementController {

    private final VideoEngagementService engagementService;

    public VideoEngagementController(VideoEngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping
    public ResponseEntity<String> getEngagementStats(@PathVariable Long videoId) {
        VideoEngagement engagement = engagementService.getEngagementStats(videoId);
        return ResponseEntity.ok("Views: " + engagement.getViews() + ", Impressions: " + engagement.getImpressions());
    }

    @PostMapping("/view")
    public ResponseEntity<String> incrementViews(@PathVariable Long videoId) {
        engagementService.incrementViewCount(videoId);
        return ResponseEntity.ok("View count incremented for video ID: " + videoId);
    }

    @PostMapping("/impression")
    public ResponseEntity<String> incrementImpressions(@PathVariable Long videoId) {
        engagementService.incrementImpressionCount(videoId);
        return ResponseEntity.ok("Impression count incremented for video ID: " + videoId);
    }
}