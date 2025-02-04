package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.entity.Video;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoRepository;
import com.example.videostreamingapi.util.VideoMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private VideoEngagementService engagementService;

    @InjectMocks
    private VideoService videoService;

    private Video testVideo;
    private VideoRequest videoRequest;
    private MockedStatic<VideoMapper> mockedMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initializeTestObjects();
        mockRepositoryMethods();
        mockVideoMapper();
    }

    @AfterEach
    void tearDown() {
        mockedMapper.close();
    }

    private void initializeTestObjects() {
        testVideo = new Video("Test Title", "Test Director", "Test Actor", "Drama", 120);
        testVideo.setId(1L);
        videoRequest = new VideoRequest("Test Title", "Test Director", "Test Actor", "Drama", 120);
    }

    private void mockRepositoryMethods() {
        when(videoRepository.findById(anyLong())).thenReturn(Optional.of(testVideo));
        when(videoRepository.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(testVideo));

        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> {
            Video savedVideo = invocation.getArgument(0);
            if (savedVideo.getId() == null)
                savedVideo.setId(1L);
            return savedVideo;
        });
    }

    private void mockVideoMapper() {
        mockedMapper = mockStatic(VideoMapper.class);
        mockedMapper.when(() -> VideoMapper.toResponse(any(Video.class))).thenAnswer(invocation -> {
            Video video = invocation.getArgument(0);
            return new VideoResponse(video.getId(), video.getTitle(), video.getDirector(),
                    video.getMainActor(), video.getGenre(), video.getRunningTime());
        });
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("listVideos should return a page of VideoResponse")
    void testListVideos() {
        Pageable pageable = mock(Pageable.class);
        Page<Video> videoPage = mock(Page.class);
        when(videoRepository.findAllByActiveTrue(pageable)).thenReturn(videoPage);
        when(videoPage.map(any())).thenReturn(mock(Page.class));

        Page<VideoResponse> response = videoService.listVideos(pageable);

        assertNotNull(response);
        verify(videoRepository).findAllByActiveTrue(pageable);
    }

    @Test
    @DisplayName("createVideo should create and return a VideoResponse")
    void testCreateVideo() {
        when(videoRepository.findByTitleAndDirector(videoRequest.getTitle(), videoRequest.getDirector()))
                .thenReturn(Optional.empty());
        when(videoRepository.save(any())).thenReturn(testVideo);
        when(VideoMapper.toResponse(any()))
                .thenReturn(new VideoResponse(1L, "Test Title", "Test Director", "Test Actor", "Drama", 120));

        VideoResponse response = videoService.createVideo(videoRequest);

        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        verify(videoRepository).save(any());
    }

    @Test
    @DisplayName("getVideoById should return VideoResponse if video exists")
    void testGetVideoById() {
        when(videoRepository.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(testVideo));

        VideoResponse response = videoService.getVideoById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        verify(engagementService).recordImpression(eq(1L));
    }

    @Test
    @DisplayName("getVideoById should throw VideoNotFoundException if video is not found")
    void testGetVideoById_NotFound() {
        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.getVideoById(1L));
    }

    @Test
    @DisplayName("playVideo should return streaming message and record view")
    void testPlayVideo() {
        when(videoRepository.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(testVideo));

        String response = videoService.playVideo(1L);

        assertEquals("Streaming video: Test Title", response);
        verify(engagementService).recordView(eq(1L));
    }

    @Test
    @DisplayName("delistVideo should deactivate video if found")
    void testDelistVideo() {
        testVideo.setActive(true);
        when(videoRepository.findById(1L)).thenReturn(Optional.of(testVideo));

        boolean result = videoService.delistVideo(1L);

        assertTrue(result);
        assertFalse(testVideo.isActive());
        verify(videoRepository).save(testVideo);
    }

    @Test
    @DisplayName("delistVideo should throw VideoNotFoundException if video is not found")
    void testDelistVideo_NotFound() {
        when(videoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.delistVideo(1L));
    }

    @Test
    @DisplayName("updateMetadata should update video metadata")
    void testUpdateMetadata() {
        when(videoRepository.findByIdAndActiveTrue(anyLong())).thenReturn(Optional.of(testVideo));

        when(videoRepository.save(any(Video.class))).thenAnswer(invocation -> {
            Video savedVideo = invocation.getArgument(0);
            savedVideo.setId(1L);
            savedVideo.setTitle("Updated Title");
            return savedVideo;
        });

        VideoRequest updateRequest = new VideoRequest("Updated Title", "Updated Director", "Updated Actor", "Sci-Fi",
                150);
        VideoResponse response = videoService.updateMetadata(1L, updateRequest);

        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        verify(videoRepository).save(any(Video.class));
    }

    @Test
    @DisplayName("updateMetadata should throw VideoNotFoundException if video is not found")
    void testUpdateMetadata_NotFound() {
        when(videoRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.empty());

        assertThrows(VideoNotFoundException.class, () -> videoService.updateMetadata(1L, videoRequest));
    }
}