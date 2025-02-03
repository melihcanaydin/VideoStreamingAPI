package com.example.videostreamingapi.entity;

import com.example.videostreamingapi.dto.request.VideoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoTest {

    private Video video;

    @BeforeEach
    void setUp() {
        video = new Video("Inception", "Christopher Nolan", "Leonardo DiCaprio", "Sci-Fi", 148);
    }

    @Test
    @DisplayName("Constructor should initialize Video fields correctly")
    void testConstructor() {
        assertEquals("Inception", video.getTitle());
        assertEquals("Christopher Nolan", video.getDirector());
        assertEquals("Leonardo DiCaprio", video.getMainActor());
        assertEquals("Sci-Fi", video.getGenre());
        assertEquals(148, video.getRunningTime());
        assertTrue(video.isActive());
        assertNotNull(video.getEngagements());
        assertEquals(0, video.getEngagements().size());
    }

    @Test
    @DisplayName("Constructor should initialize Video from VideoRequest correctly")
    void testConstructorFromVideoRequest() {
        VideoRequest request = new VideoRequest("Interstellar", "Christopher Nolan", "Matthew McConaughey", "Sci-Fi",
                169);
        Video videoFromRequest = new Video(request);

        assertEquals("Interstellar", videoFromRequest.getTitle());
        assertEquals("Christopher Nolan", videoFromRequest.getDirector());
        assertEquals("Matthew McConaughey", videoFromRequest.getMainActor());
        assertEquals("Sci-Fi", videoFromRequest.getGenre());
        assertEquals(169, videoFromRequest.getRunningTime());
        assertTrue(videoFromRequest.isActive());
    }

    @Test
    @DisplayName("updateMetadata should update Video fields correctly")
    void testUpdateMetadata() {
        VideoRequest updateRequest = new VideoRequest("Dune", "Denis Villeneuve", "Timothée Chalamet", "Sci-Fi", 155);
        video.updateMetadata(updateRequest);

        assertEquals("Dune", video.getTitle());
        assertEquals("Denis Villeneuve", video.getDirector());
        assertEquals("Timothée Chalamet", video.getMainActor());
        assertEquals("Sci-Fi", video.getGenre());
        assertEquals(155, video.getRunningTime());
    }

    @Test
    @DisplayName("Default constructor should initialize an empty video object")
    void testDefaultConstructor() {
        Video emptyVideo = new Video();
        assertNull(emptyVideo.getTitle());
        assertNull(emptyVideo.getDirector());
        assertNull(emptyVideo.getMainActor());
        assertNull(emptyVideo.getGenre());
        assertEquals(0, emptyVideo.getRunningTime());
        assertTrue(emptyVideo.isActive());
        assertNotNull(emptyVideo.getEngagements());
        assertEquals(0, emptyVideo.getEngagements().size());
    }

    @Test
    @DisplayName("Video should initialize with an empty list of engagements")
    void testEngagementListInitialization() {
        assertNotNull(video.getEngagements());
        assertEquals(0, video.getEngagements().size());
    }

    @Test
    @DisplayName("Engagements list should allow adding and removing engagements")
    void testEngagementsModification() {
        VideoEngagement engagement1 = new VideoEngagement(video, 10, 5);
        VideoEngagement engagement2 = new VideoEngagement(video, 20, 8);

        video.getEngagements().add(engagement1);
        video.getEngagements().add(engagement2);

        assertEquals(2, video.getEngagements().size());
        assertTrue(video.getEngagements().contains(engagement1));
        assertTrue(video.getEngagements().contains(engagement2));

        video.getEngagements().remove(engagement1);
        assertEquals(1, video.getEngagements().size());
        assertFalse(video.getEngagements().contains(engagement1));
    }
}