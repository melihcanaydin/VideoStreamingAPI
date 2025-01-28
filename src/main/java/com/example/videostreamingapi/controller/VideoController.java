package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {
    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @GetMapping
    public List<Video> getVideos() {
        logger.info("GET /videos - Fetching all videos");
        return videoService.getAllVideos();
    }

    @PostMapping
    public Video createVideo(@RequestBody Video video) {
        logger.info("POST /videos - Creating a new video");
        return videoService.createVideo(video);
    }

    @DeleteMapping("/{id}")
    public void deleteVideo(@PathVariable Long id) {
        logger.info("DELETE /videos/{} - Deleting a video", id);
        videoService.deleteVideo(id);
    }

    @PutMapping("/{id}")
    public Video updateVideo(@PathVariable Long id, @RequestBody Video video) {
        logger.info("PUT /videos/{} - Updating a video", id);
        return videoService.updateVideo(id, video);
    }
}