package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.entity.VideoEngagement;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoEngagementServiceTest {

    @Mock
    private VideoEngagementRepository engagementRepository;

    @InjectMocks
    private VideoEngagementService engagementService;

    private final Long testVideoId = 1L;
    private VideoEngagement testEngagement;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testEngagement = new VideoEngagement();
        testEngagement.setId(1L);
        testEngagement.setViews(10);
        testEngagement.setImpressions(50);

        when(engagementRepository.findByVideoId(testVideoId)).thenReturn(Optional.of(testEngagement));
    }

    @Test
    @DisplayName("recordView should update view count in repository")
    void testRecordView() {
        doNothing().when(engagementRepository).upsertView(testVideoId);

        engagementService.recordView(testVideoId);

        verify(engagementRepository, times(1)).upsertView(testVideoId);
    }

    @Test
    @DisplayName("recordImpression should update impression count in repository")
    void testRecordImpression() {
        doNothing().when(engagementRepository).upsertImpression(testVideoId);

        engagementService.recordImpression(testVideoId);

        verify(engagementRepository, times(1)).upsertImpression(testVideoId);
    }

    @Test
    @DisplayName("getEngagementStats should return VideoEngagementResponse if data exists")
    void testGetEngagementStats() {
        VideoEngagementResponse response = engagementService.getEngagementStats(testVideoId);

        assertNotNull(response);
        assertEquals(testVideoId, response.getVideoId());
        assertEquals(10, response.getViews());
        assertEquals(50, response.getImpressions());
    }

    @Test
    @DisplayName("getEngagementStats should throw VideoNotFoundException if no engagement data found")
    void testGetEngagementStats_NotFound() {
        when(engagementRepository.findByVideoId(999L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> engagementService.getEngagementStats(999L));
    }
}