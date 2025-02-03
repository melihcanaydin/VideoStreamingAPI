package com.example.videostreamingapi.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VideoEngagementTest {

    private Video video;
    private VideoEngagement engagement;

    @BeforeEach
    void setUp() {
        video = new Video("Inception", "Christopher Nolan", "Leonardo DiCaprio", "Sci-Fi", 148);
        engagement = new VideoEngagement(video, 100, 50);
    }

    @Test
    @DisplayName("Constructor should initialize VideoEngagement fields correctly")
    void testConstructor() {
        assertEquals(video, engagement.getVideo());
        assertEquals(100, engagement.getViews());
        assertEquals(50, engagement.getImpressions());
    }

    @Test
    @DisplayName("Default constructor should initialize fields correctly")
    void testDefaultConstructor() {
        VideoEngagement emptyEngagement = new VideoEngagement();
        assertNull(emptyEngagement.getVideo());
        assertEquals(0, emptyEngagement.getViews());
        assertEquals(0, emptyEngagement.getImpressions());
    }

    @Test
    @DisplayName("incrementViews should increase views count")
    void testIncrementViews() {
        engagement.incrementViews();
        assertEquals(101, engagement.getViews());

        engagement.incrementViews();
        assertEquals(102, engagement.getViews());
    }

    @Test
    @DisplayName("incrementImpressions should increase impressions count")
    void testIncrementImpressions() {
        engagement.incrementImpressions();
        assertEquals(51, engagement.getImpressions());

        engagement.incrementImpressions();
        assertEquals(52, engagement.getImpressions());
    }
}