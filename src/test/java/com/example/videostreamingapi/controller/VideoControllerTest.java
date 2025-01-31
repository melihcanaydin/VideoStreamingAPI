package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.service.VideoService;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VideoService videoService;

    @Mock
    private VideoEngagementService engagementService;

    @InjectMocks
    private VideoController videoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();
    }

    @Test
    void listVideos_ReturnsListOfVideos() throws Exception {
        Video video1 = createMockVideo(1L, "Video 1", "Director 1", "Action", 2023, 120, true);
        Video video2 = createMockVideo(2L, "Video 2", "Director 2", "Drama", 2022, 110, true);
        List<Video> videos = Arrays.asList(video1, video2);

        when(videoService.listVideos()).thenReturn(videos);

        mockMvc.perform(get("/videos/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Video 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Video 2"));

        verify(videoService, times(1)).listVideos();
    }

    @Test
    void publishVideo_ReturnsCreatedVideo() throws Exception {
        Video video = createMockVideo(1L, "New Video", "Director A", "Comedy", 2024, 90, true);
        when(videoService.publishVideo(any(Video.class))).thenReturn(video);

        mockMvc.perform(post("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"title\":\"New Video\",\"director\":\"Director A\",\"genre\":\"Comedy\",\"year\":2024,\"runningTime\":90,\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Video"));

        verify(videoService, times(1)).publishVideo(any(Video.class));
    }

    @Test
    void loadVideo_ReturnsVideoAndIncrementsImpressions() throws Exception {
        Long videoId = 1L;
        Video video = createMockVideo(videoId, "Loaded Video", "Director B", "Sci-Fi", 2021, 130, true);
        when(videoService.getVideoById(videoId)).thenReturn(video);

        mockMvc.perform(get("/videos/{id}", videoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(videoId))
                .andExpect(jsonPath("$.title").value("Loaded Video"));

        verify(videoService, times(1)).getVideoById(videoId);
        verify(engagementService, times(1)).incrementImpressionCount(videoId);
    }

    @Test
    void playVideo_ReturnsPlayResponse() throws Exception {
        Long videoId = 1L;
        String response = "Playing video 1";
        when(videoService.playVideo(videoId)).thenReturn(response);

        mockMvc.perform(post("/videos/{id}/play", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

        verify(videoService, times(1)).playVideo(videoId);
    }

    @Test
    void delistVideo_DeletesVideo() throws Exception {
        Long videoId = 1L;
        doNothing().when(videoService).delistVideo(videoId);

        mockMvc.perform(delete("/videos/{id}", videoId))
                .andExpect(status().isOk());

        verify(videoService, times(1)).delistVideo(videoId);
    }

    @Test
    void updateMetadata_ReturnsUpdatedVideo() throws Exception {
        Long videoId = 1L;
        Video updatedVideo = createMockVideo(videoId, "Updated Title", "Updated Director", "Thriller", 2025, 100, true);
        when(videoService.updateMetadata(eq(videoId), any(Video.class))).thenReturn(updatedVideo);

        mockMvc.perform(put("/videos/{id}/metadata", videoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"title\":\"Updated Title\",\"director\":\"Updated Director\",\"genre\":\"Thriller\",\"year\":2025,\"runningTime\":100,\"active\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(videoId))
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(videoService, times(1)).updateMetadata(eq(videoId), any(Video.class));
    }

    private Video createMockVideo(Long id, String title, String director, String genre, int year, int runningTime,
            boolean active) {
        Video video = new Video();
        video.setId(id);
        video.setTitle(title);
        video.setDirector(director);
        video.setGenre(genre);
        video.setYear(year);
        video.setRunningTime(runningTime);
        video.setActive(active);
        return video;
    }
}