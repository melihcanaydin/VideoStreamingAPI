package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.response.VideoEngagementResponse;
import com.example.videostreamingapi.entity.VideoEngagement;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoEngagementService {
    private static final Logger logger = LoggerFactory.getLogger(VideoEngagementService.class);

    private final VideoEngagementRepository engagementRepository;

    public VideoEngagementService(VideoEngagementRepository engagementRepository) {
        this.engagementRepository = engagementRepository;
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
        if (isView) {
            engagementRepository.upsertView(videoId);
            logger.info("View count updated for video ID: {}", videoId);
        } else {
            engagementRepository.upsertImpression(videoId);
            logger.info("Impression count updated for video ID: {}", videoId);
        }
    }
}