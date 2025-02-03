package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private VideoService videoService;

    private VideoResponse sampleVideo;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        sampleVideo = new VideoResponse(1L, "Sample Video", "Director", "Actor", "Action", 120);
    }

    @Test
    @DisplayName("GET /videos/{id} -> Should return video by ID")
    void testGetVideoById() throws Exception {
        when(videoService.getVideoById(1L)).thenReturn(sampleVideo);

        mockMvc.perform(get("/videos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Video"));
    }

    @Test
    @DisplayName("GET /videos/{videoId}/play -> Should return play confirmation")
    void testPlayVideo() throws Exception {
        when(videoService.playVideo(1L)).thenReturn("Playing video 1");

        mockMvc.perform(get("/videos/1/play"))
                .andExpect(status().isOk())
                .andExpect(content().string("Playing video 1"));
    }

    @Test
    @DisplayName("DELETE /videos/{id} -> Should delist a video and return 204")
    void testDelistVideo() throws Exception {
        when(videoService.delistVideo(1L)).thenReturn(true);

        mockMvc.perform(delete("/videos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /videos/{id} -> Should return 404 if video not found")
    void testDelistNonExistingVideo() throws Exception {
        when(videoService.delistVideo(999L)).thenReturn(false);

        mockMvc.perform(delete("/videos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /videos/{id}/metadata -> Should update video metadata")
    void testUpdateMetadata() throws Exception {
        String updateJson = """
                {
                    "title": "Updated Video",
                    "director": "New Director",
                    "mainActor": "New Actor",
                    "genre": "Sci-Fi",
                    "runningTime": 130
                }
                """;

        when(videoService.updateMetadata(eq(1L), any(VideoRequest.class)))
                .thenReturn(new VideoResponse(1L, "Updated Video", "New Director", "New Actor", "Sci-Fi", 130));

        mockMvc.perform(put("/videos/1/metadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Video"))
                .andExpect(jsonPath("$.genre").value("Sci-Fi"));
    }

    @Test
    @DisplayName("PUT /videos/{id}/metadata -> Should return 404 if video not found")
    void testUpdateMetadataNonExisting() throws Exception {
        String updateJson = """
                {
                    "title": "Nonexistent Video",
                    "director": "Director",
                    "mainActor": "Actor",
                    "genre": "Horror",
                    "runningTime": 90
                }
                """;

        when(videoService.updateMetadata(eq(999L), any(VideoRequest.class))).thenReturn(null);

        mockMvc.perform(put("/videos/999/metadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
                .andExpect(status().isNotFound());
    }
}