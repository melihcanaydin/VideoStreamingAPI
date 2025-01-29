package com.example.videostreamingapi.service;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    private final VideoRepository videoRepository = mock(VideoRepository.class);
    private final VideoService videoService = new VideoService(videoRepository);

    @Test
    void whenCreateVideo_ThenSaveVideo() {
        Video video = new Video();
        video.setTitle("Test Video");

        when(videoRepository.save(video)).thenReturn(video);

        Video result = videoService.createVideo(video);

        assertEquals("Test Video", result.getTitle());
        verify(videoRepository, times(1)).save(video);
    }
}
