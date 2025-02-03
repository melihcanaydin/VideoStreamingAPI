package com.example.videostreamingapi.integration;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.entity.Video;
import com.example.videostreamingapi.entity.VideoEngagement;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import com.example.videostreamingapi.repository.VideoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VideoEngagementControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private VideoEngagementRepository videoEngagementRepository;

    private Video testVideo;

    @BeforeEach
    void setup() {
        videoEngagementRepository.deleteAll();
        videoRepository.deleteAll();

        testVideo = new Video("Test Video", "Director", "Main Actor", "Action", 100);
        testVideo = videoRepository.save(testVideo);

        System.out.println("Created Test Video ID: " + testVideo.getId());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Integration: GET /videos/{videoId}/engagement -> should return engagement stats")
    void testGetEngagementStats() {
        videoEngagementRepository.save(new VideoEngagement(testVideo, 100, 50));

        ResponseEntity<VideoEngagementResponse> response = restTemplate.getForEntity(
                "/videos/{videoId}/engagement", VideoEngagementResponse.class, testVideo.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(100, response.getBody().getViews());
        assertEquals(50, response.getBody().getImpressions());
    }

    @Test
    @DisplayName("Integration: GET /videos/{videoId}/engagement -> should return 404 for non-existing video")
    void testGetEngagementStatsForNonExistingVideo() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/videos/999/engagement", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}