package com.example.videostreamingapi.service;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import com.example.videostreamingapi.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VideoEngagementServiceTest {

    private final VideoRepository videoRepository = mock(VideoRepository.class);
    private final VideoEngagementRepository engagementRepository = mock(VideoEngagementRepository.class);
    private final VideoEngagementService engagementService = new VideoEngagementService(engagementRepository,
            videoRepository);

    @Test
    void whenViewIncremented_ThenViewCountUpdates() {
        Video video = new Video();
        video.setId(1L);
        when(videoRepository.findById(1L)).thenReturn(java.util.Optional.of(video));

        VideoEngagement engagement = new VideoEngagement();
        engagement.setVideo(video);
        engagement.setViews(5);
        when(engagementRepository.findByVideoId(1L)).thenReturn(java.util.Optional.of(engagement));

        engagementService.incrementViewCount(1L);
        assertEquals(6, engagement.getViews());
    }
}
