package com.example.videostreamingapi.service;

import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.model.VideoEngagement;
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

        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> {
                    logger.warn("Engagement data not found for video ID: {}, initializing new entry.", videoId);
                    Video video = videoRepository.findByIdAndActiveTrue(videoId)
                            .orElseThrow(() -> {
                                logger.error("Active video not found with ID: {}", videoId);
                                return new VideoNotFoundException("Active video not found with ID: " + videoId);
                            });

                    VideoEngagement newEngagement = new VideoEngagement();
                    newEngagement.setVideo(video);
                    return newEngagement;
                });

        engagement.incrementViews();
        engagementRepository.save(engagement);

        logger.info("Successfully updated view count for video ID: {}. New views: {}", videoId, engagement.getViews());
    }

    @Transactional
    public void incrementImpressionCount(Long videoId) {
        logger.info("Incrementing impression count for video ID: {}", videoId);

        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> {
                    logger.warn("Engagement data not found for video ID: {}, initializing new entry.", videoId);
                    Video video = videoRepository.findByIdAndActiveTrue(videoId)
                            .orElseThrow(() -> {
                                logger.error("Active video not found with ID: {}", videoId);
                                return new VideoNotFoundException("Active video not found with ID: " + videoId);
                            });

                    VideoEngagement newEngagement = new VideoEngagement();
                    newEngagement.setVideo(video);
                    return newEngagement;
                });

        engagement.incrementImpressions();
        engagementRepository.save(engagement);

        logger.info("Successfully updated impression count for video ID: {}. New impressions: {}", videoId,
                engagement.getImpressions());
    }

    public VideoEngagement getEngagementStats(Long videoId) {
        logger.info("Fetching engagement stats for video ID: {}", videoId);

        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseThrow(() -> {
                    logger.warn("No engagement data found for video ID: {}", videoId);
                    return new VideoNotFoundException("No engagement data found for this video");
                });

        logger.info("Engagement stats retrieved for video ID: {}. Views: {}, Impressions: {}", videoId,
                engagement.getViews(), engagement.getImpressions());
        return engagement;
    }
}