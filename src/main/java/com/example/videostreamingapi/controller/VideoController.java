package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.service.VideoService;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private final VideoService videoService;
    private final VideoEngagementService engagementService;

    public VideoController(VideoService videoService, VideoEngagementService engagementService) {
        this.videoService = videoService;
        this.engagementService = engagementService;
    }

    @GetMapping("/list")
    public List<Video> listVideos() {
        return videoService.listVideos();
    }

    @PostMapping
    public Video publishVideo(@RequestBody Video video) {
        return videoService.publishVideo(video);
    }

    @GetMapping("/{id}")
    public Video loadVideo(@PathVariable Long id) {
        Video video = videoService.getVideoById(id);

        engagementService.incrementImpressionCount(id);
        return video;
    }

    @PostMapping("/{id}/play")
    public ResponseEntity<String> playVideo(@PathVariable Long id) {
        String response = videoService.playVideo(id);

        engagementService.incrementViewCount(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public void delistVideo(@PathVariable Long id) {
        videoService.delistVideo(id);
    }

    @PutMapping("/{id}/metadata")
    public Video updateMetadata(@PathVariable Long id, @RequestBody Video video) {
        return videoService.updateMetadata(id, video);
    }
}