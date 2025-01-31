package com.example.videostreamingapi.service;

import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import com.example.videostreamingapi.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoEngagementServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoEngagementRepository engagementRepository;

    @InjectMocks
    private VideoEngagementService engagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenIncrementViewCount_ThenViewCountUpdates() {
        Video video = new Video();
        video.setId(1L);
        video.setActive(true);

        VideoEngagement engagement = new VideoEngagement();
        engagement.setVideo(video);
        engagement.setViews(5);

        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));

        engagementService.incrementViewCount(1L);

        assertEquals(6, engagement.getViews());
        verify(engagementRepository, times(1)).save(engagement);
    }

    @Test
    void whenIncrementViewCount_AndNoExistingEngagement_ThenCreateNewEngagement() {
        Video video = new Video();
        video.setId(1L);
        video.setActive(true);

        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.empty());

        engagementService.incrementViewCount(1L);

        verify(engagementRepository, times(1)).save(any(VideoEngagement.class));
    }

    @Test
    void whenIncrementViewCount_AndVideoNotFound_ThenThrowException() {
        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> engagementService.incrementViewCount(1L));
    }

    @Test
    void whenIncrementImpressionCount_ThenImpressionCountUpdates() {
        Video video = new Video();
        video.setId(1L);
        video.setActive(true);

        VideoEngagement engagement = new VideoEngagement();
        engagement.setVideo(video);
        engagement.setImpressions(3);

        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));

        engagementService.incrementImpressionCount(1L);

        assertEquals(4, engagement.getImpressions());
        verify(engagementRepository, times(1)).save(engagement);
    }

    @Test
    void whenIncrementImpressionCount_AndNoExistingEngagement_ThenCreateNewEngagement() {
        Video video = new Video();
        video.setId(1L);
        video.setActive(true);

        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.empty());

        engagementService.incrementImpressionCount(1L);

        verify(engagementRepository, times(1)).save(any(VideoEngagement.class));
    }

    @Test
    void whenIncrementImpressionCount_AndVideoNotFound_ThenThrowException() {
        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> engagementService.incrementImpressionCount(1L));
    }
}