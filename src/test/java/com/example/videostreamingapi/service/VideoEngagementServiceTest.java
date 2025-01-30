package com.example.videostreamingapi.service;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
        VideoEngagement engagement = new VideoEngagement();
        engagement.setVideo(video);
        engagement.setViews(5);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));

        engagementService.incrementViewCount(1L);

        assertEquals(6, engagement.getViews());
        verify(engagementRepository, times(1)).save(engagement);
    }

    @Test
    void whenIncrementLikeCount_ThenLikeCountUpdates() {
        Video video = new Video();
        video.setId(1L);
        VideoEngagement engagement = new VideoEngagement();
        engagement.setVideo(video);
        engagement.setLikes(2);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));
        when(engagementRepository.findByVideoId(1L)).thenReturn(Optional.of(engagement));

        engagementService.incrementLikeCount(1L);

        assertEquals(3, engagement.getLikes());
        verify(engagementRepository, times(1)).save(engagement);
    }
}