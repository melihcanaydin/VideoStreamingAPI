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
}