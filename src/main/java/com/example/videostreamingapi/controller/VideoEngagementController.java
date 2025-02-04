package com.example.videostreamingapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.service.VideoEngagementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/videos")
public class VideoEngagementController {

    private final VideoEngagementService engagementService;

    public VideoEngagementController(VideoEngagementService engagementService) {
        this.engagementService = engagementService;
    }

    @Operation(summary = "Get engagement statistics", description = "Retrieve view and impression count for a given video.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Engagement statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @GetMapping("/{id}/engagement")
    public ResponseEntity<VideoEngagementResponse> getEngagementStats(@PathVariable Long videoId) {
        VideoEngagementResponse response = engagementService.getEngagementStats(videoId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(response);
    }
}