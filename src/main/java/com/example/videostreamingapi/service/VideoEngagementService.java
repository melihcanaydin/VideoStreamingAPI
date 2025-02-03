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
import org.springframework.transaction.annotation.Transactional;

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
    public void recordView(Long videoId) {
        updateEngagementStats(videoId, true);
    }

    @Transactional
    public void recordImpression(Long videoId) {
        updateEngagementStats(videoId, false);
    }

    public VideoEngagementResponse getEngagementStats(Long videoId) {
        logger.info("Fetching engagement stats for video ID: {}", videoId);
        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoNotFoundException("No engagement data found for video ID: " + videoId));

        return new VideoEngagementResponse(videoId, engagement.getViews(), engagement.getImpressions());
    }

    @Transactional
    private void updateEngagementStats(Long videoId, boolean isView) {
        int updatedRows = isView ? engagementRepository.incrementViewCount(videoId)
                : engagementRepository.incrementImpressionCount(videoId);

        if (updatedRows == 0) {
            VideoEngagement engagement = findOrCreateEngagement(videoId);

            if (isView) {
                engagement.setViews(engagement.getViews() + 1);
                logger.info("Created engagement and updated view count for video ID: {}. Total views: {}", videoId,
                        engagement.getViews());
            } else {
                engagement.setImpressions(engagement.getImpressions() + 1);
                logger.info("Created engagement and updated impression count for video ID: {}. Total impressions: {}",
                        videoId, engagement.getImpressions());
            }

            engagementRepository.save(engagement);
        }
    }

    private VideoEngagement findOrCreateEngagement(Long videoId) {
        return engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> createNewEngagement(videoId));
    }

    private VideoEngagement createNewEngagement(Long videoId) {
        Video video = videoRepository.findByIdAndActiveTrue(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Active video not found with ID: " + videoId));

        VideoEngagement newEngagement = new VideoEngagement();
        newEngagement.setVideo(video);
        return engagementRepository.save(newEngagement);
    }
}