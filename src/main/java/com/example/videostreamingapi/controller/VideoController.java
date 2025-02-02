package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.service.VideoEngagementService;
import com.example.videostreamingapi.service.VideoService;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoService videoService;
    private final VideoEngagementService engagementService;

    public VideoController(VideoService videoService, VideoEngagementService engagementService) {
        this.videoService = videoService;
        this.engagementService = engagementService;
    }

    @GetMapping("/list")
    public ResponseEntity<Page<VideoResponse>> getAllVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.listVideos(pageable));
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<VideoResponse>> searchVideos(
            @RequestParam(name = "query") String query,
            Pageable pageable) {
        return ResponseEntity.ok(videoService.searchVideos(query, pageable));
    }

    @PostMapping
    public ResponseEntity<VideoResponse> createVideo(@Valid @RequestBody VideoRequest videoRequest) {
        VideoResponse createdVideo = videoService.createVideo(videoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }

    @GetMapping(value = "/{id:[0-9]+}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        VideoResponse video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }

    @GetMapping("/{videoId}/play")
    public ResponseEntity<String> playVideo(@PathVariable Long videoId) {
        logger.info("Received request to play video with ID: {}", videoId);
        return ResponseEntity.ok(videoService.playVideo(videoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        boolean deleted = videoService.delistVideo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<VideoResponse> updateMetadata(@PathVariable Long id,
            @Valid @RequestBody VideoRequest videoRequest) {
        VideoResponse updatedVideo = videoService.updateMetadata(id, videoRequest);
        return updatedVideo != null ? ResponseEntity.ok(updatedVideo) : ResponseEntity.notFound().build();
    }
}