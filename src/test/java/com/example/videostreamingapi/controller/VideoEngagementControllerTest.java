package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.service.VideoEngagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VideoEngagementControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VideoEngagementService engagementService;

    @InjectMocks
    private VideoEngagementController videoEngagementController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(videoEngagementController).build();
    }

    @Test
    void getEngagementStats_ReturnsCorrectEngagementData() throws Exception {
        Long videoId = 1L;
        VideoEngagement engagement = new VideoEngagement();
        engagement.setViews(150);
        engagement.setImpressions(300);

        when(engagementService.getEngagementStats(videoId)).thenReturn(engagement);

        mockMvc.perform(get("/videos/{videoId}/engagement", videoId))
                .andExpect(status().isOk())
                .andExpect(content().string("Views: 150, Impressions: 300"));

        verify(engagementService, times(1)).getEngagementStats(videoId);
    }
}