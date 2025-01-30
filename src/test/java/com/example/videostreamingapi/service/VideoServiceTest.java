package com.example.videostreamingapi.service;

import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private VideoService videoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenCreateVideo_ThenVideoIsSaved() {
        Video video = new Video();
        video.setTitle("Test Video");

        when(videoRepository.save(any(Video.class))).thenReturn(video);

        Video result = videoService.createVideo(video);

        assertNotNull(result);
        assertEquals("Test Video", result.getTitle());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void whenGetVideoById_ThenReturnVideo() {
        Video video = new Video();
        video.setId(1L);
        video.setTitle("Test Video");

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        Video result = videoService.getVideoById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Video", result.getTitle());
    }

    @Test
    void whenGetVideoById_AndNotFound_ThenThrowException() {
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.getVideoById(1L));
    }

    @Test
    void whenDeleteVideo_ThenMarkAsInactive() {
        Video video = new Video();
        video.setId(1L);
        video.setActive(true);

        when(videoRepository.findById(1L)).thenReturn(Optional.of(video));

        videoService.deleteVideo(1L);

        assertFalse(video.isActive());
        verify(videoRepository, times(1)).save(video);
    }
}