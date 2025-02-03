package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.service.VideoEngagementService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoEngagementController {
    private final VideoEngagementService engagementService;

    public VideoEngagementController(VideoEngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @GetMapping("/{videoId}/engagement")
    public ResponseEntity<VideoEngagementResponse> getEngagementStats(@PathVariable Long videoId) {
        VideoEngagementResponse response = engagementService.getEngagementStats(videoId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(response);
    }
}