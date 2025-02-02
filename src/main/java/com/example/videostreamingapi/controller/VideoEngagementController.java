package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
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
    public ResponseEntity<VideoEngagementResponse> getEngagementStats(@PathVariable Long videoId) {
        VideoEngagementResponse engagement = engagementService.getEngagementStats(videoId);
        return ResponseEntity.ok(engagement);
    }

    @PostMapping("/view")
    public ResponseEntity<Void> incrementViews(@PathVariable Long videoId) {
        engagementService.incrementViewCount(videoId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/impression")
    public ResponseEntity<Void> incrementImpressions(@PathVariable Long videoId) {
        engagementService.incrementImpressionCount(videoId);
        return ResponseEntity.noContent().build();
    }
}