package com.example.videostreamingapi.integration;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
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
class VideoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private VideoRepository videoRepository;

    @BeforeEach
    void setup() {
        videoRepository.deleteAll();
    }

    @Test
    @DisplayName("Integration: GET /videos/list -> returns empty list initially")
    void testGetAllVideos_Empty() {
        ResponseEntity<String> response = restTemplate.getForEntity("/videos/list", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Integration: POST /videos -> create and then GET -> find created video")
    void testCreateAndGetVideo() {
        VideoRequest videoRequest = new VideoRequest("Integration Video", "Director", "Main Actor", "Action", 120);
        ResponseEntity<VideoResponse> createResponse = restTemplate.postForEntity("/videos",
                videoRequest, VideoResponse.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        VideoResponse createdVideo = createResponse.getBody();
        assertNotNull(createdVideo);
        assertNotNull(createdVideo.getId());

        ResponseEntity<VideoResponse> getResponse = restTemplate.getForEntity("/videos/{id}",
                VideoResponse.class, createdVideo.getId());
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Integration Video", getResponse.getBody().getTitle());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Integration: GET /videos/{id} -> retrieves video by ID")
    void testGetVideoById() {
        VideoRequest videoRequest = new VideoRequest("Test Video", "Director", "Main Actor", "Action", 90);
        VideoResponse createdVideo = restTemplate.postForEntity("/videos", videoRequest, VideoResponse.class).getBody();
        assertNotNull(createdVideo);
        Long videoId = createdVideo.getId();

        ResponseEntity<VideoResponse> response = restTemplate.getForEntity("/videos/{id}", VideoResponse.class,
                videoId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Video", response.getBody().getTitle());
    }

    @SuppressWarnings("null")
    @Test
    @DisplayName("Integration: PUT /videos/{id}/metadata -> updates video metadata")
    void testUpdateVideoMetadata() {
        VideoRequest videoRequest = new VideoRequest("Old Title", "Director", "Main Actor", "Action", 100);
        VideoResponse createdVideo = restTemplate.postForEntity("/videos", videoRequest, VideoResponse.class).getBody();
        assertNotNull(createdVideo);
        Long videoId = createdVideo.getId();

        VideoRequest updatedRequest = new VideoRequest("Updated Title", "New Director", "New Actor", "Sci-Fi", 130);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VideoRequest> requestEntity = new HttpEntity<>(updatedRequest, headers);

        ResponseEntity<VideoResponse> updateResponse = restTemplate.exchange(
                "/videos/{id}/metadata", HttpMethod.PUT, requestEntity, VideoResponse.class, videoId);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Updated Title", updateResponse.getBody().getTitle());
    }

    @Test
    @DisplayName("Integration: DELETE /videos/{id} -> deletes a video")
    void testDeleteVideo() {
        VideoRequest videoRequest = new VideoRequest("Video to Delete", "Director", "Actor", "Drama", 120);
        VideoResponse createdVideo = restTemplate.postForEntity("/videos", videoRequest, VideoResponse.class).getBody();
        assertNotNull(createdVideo);
        Long videoId = createdVideo.getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/videos/{id}",
                HttpMethod.DELETE, null, Void.class, videoId);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<String> getResponse = restTemplate.getForEntity("/videos/{id}", String.class, videoId);

        System.out.println("GET after DELETE Response: " + getResponse.getStatusCode() + " - " + getResponse.getBody());

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}