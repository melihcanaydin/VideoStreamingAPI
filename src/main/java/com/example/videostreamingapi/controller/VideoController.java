package com.example.videostreamingapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.service.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Operation(summary = "List all videos", description = "Retrieve a paginated list of all active videos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval of videos"),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters")
    })
    @GetMapping("/list")
    public ResponseEntity<Page<VideoResponse>> getAllVideos(Pageable pageable) {
        return ResponseEntity.ok(videoService.listVideos(pageable));
    }

    @Operation(summary = "Search videos", description = "Search videos by title, genre, or director.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results returned successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search query")
    })
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<VideoResponse>> searchVideos(
            @Parameter(description = "Search query string") @RequestParam(name = "query") String query,
            Pageable pageable) {
        return ResponseEntity.ok(videoService.searchVideos(query, pageable));
    }

    @Operation(summary = "Create a new video", description = "Add a new video to the catalog.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Video created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid video request payload")
    })
    @PostMapping
    public ResponseEntity<VideoResponse> createVideo(@RequestBody VideoRequest videoRequest) {
        VideoResponse createdVideo = videoService.createVideo(videoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVideo);
    }

    @Operation(summary = "Get video by ID", description = "Retrieve video details by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Video found"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<VideoResponse> getVideoById(@PathVariable Long id) {
        VideoResponse video = videoService.getVideoById(id);
        return ResponseEntity.ok(video);
    }

    @Operation(summary = "Play a video", description = "Simulate video streaming by video ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Streaming started successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @GetMapping("/{videoId}/play")
    public ResponseEntity<String> playVideo(@PathVariable Long videoId) {
        return ResponseEntity.ok(videoService.playVideo(videoId));
    }

    @Operation(summary = "Delete a video", description = "Soft delete a video by marking it inactive.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Video deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideo(@PathVariable Long id) {
        boolean deleted = videoService.delistVideo(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Update video metadata", description = "Update metadata such as title, director, genre, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Metadata updated successfully"),
            @ApiResponse(responseCode = "404", description = "Video not found")
    })
    @PutMapping("/{id}/metadata")
    public ResponseEntity<VideoResponse> updateMetadata(@PathVariable Long id,
            @RequestBody VideoRequest videoRequest) {
        VideoResponse updatedVideo = videoService.updateMetadata(id, videoRequest);
        return updatedVideo != null ? ResponseEntity.ok(updatedVideo) : ResponseEntity.notFound().build();
    }
}