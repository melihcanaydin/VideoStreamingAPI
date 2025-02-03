package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class VideoEngagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
    @MockBean
    private VideoEngagementService engagementService;

    @Test
    @DisplayName("GET /videos/{videoId}/engagement -> Should return engagement stats")
    void testGetEngagementStats() throws Exception {
        VideoEngagementResponse mockResponse = new VideoEngagementResponse(1L, 100, 50);

        when(engagementService.getEngagementStats(1L)).thenReturn(mockResponse);

        mockMvc.perform(get("/videos/1/engagement")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.videoId").value(1))
                .andExpect(jsonPath("$.views").value(100))
                .andExpect(jsonPath("$.impressions").value(50));
    }

    @Test
    @DisplayName("GET /videos/{videoId}/engagement -> Should return 404 if video not found")
    void testGetEngagementStatsForNonExistingVideo() throws Exception {
        when(engagementService.getEngagementStats(anyLong())).thenReturn(null);

        mockMvc.perform(get("/videos/999/engagement"))
                .andExpect(status().isNotFound());
    }
}