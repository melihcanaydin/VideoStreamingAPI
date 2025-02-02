package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.entity.Video;
import com.example.videostreamingapi.entity.VideoEngagement;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import com.example.videostreamingapi.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class VideoEngagementService {
    private static final Logger logger = LoggerFactory.getLogger(VideoEngagementService.class);

    private final VideoEngagementRepository engagementRepository;
    private final VideoRepository videoRepository;

    public VideoEngagementService(VideoEngagementRepository engagementRepository, VideoRepository videoRepository) {
        this.engagementRepository = engagementRepository;
        this.videoRepository = videoRepository;
    }

    @Transactional
    public void incrementViewCount(Long videoId) {
        logger.info("Incrementing view count for video ID: {}", videoId);
        VideoEngagement engagement = findOrCreateEngagement(videoId);
        engagement.incrementViews();
        engagementRepository.save(engagement);
        logger.info("View count updated for video ID: {}. New views: {}", videoId, engagement.getViews());
    }

    @Transactional
    public void incrementImpressionCount(Long videoId) {
        logger.info("Incrementing impression count for video ID: {}", videoId);
        VideoEngagement engagement = findOrCreateEngagement(videoId);
        engagement.incrementImpressions();
        engagementRepository.save(engagement);
        logger.info("Impression count updated for video ID: {}. New impressions: {}", videoId,
                engagement.getImpressions());
    }

    public VideoEngagementResponse getEngagementStats(Long videoId) {
        logger.info("Fetching engagement stats for video ID: {}", videoId);
        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoNotFoundException("No engagement data found for video ID: " + videoId));

        return new VideoEngagementResponse(videoId, engagement.getViews(), engagement.getImpressions());
    }

    private VideoEngagement findOrCreateEngagement(Long videoId) {
        return engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> {
                    Video video = videoRepository.findByIdAndActiveTrue(videoId)
                            .orElseThrow(
                                    () -> new VideoNotFoundException("Active video not found with ID: " + videoId));

                    VideoEngagement newEngagement = new VideoEngagement();
                    newEngagement.setVideo(video);
                    return engagementRepository.save(newEngagement);
                });
    }
}