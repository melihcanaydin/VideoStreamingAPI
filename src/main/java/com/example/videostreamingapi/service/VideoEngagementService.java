package com.example.videostreamingapi.service;

import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.model.VideoEngagement;
import com.example.videostreamingapi.repository.VideoEngagementRepository;
import com.example.videostreamingapi.repository.VideoRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class VideoEngagementService {
    private final VideoEngagementRepository engagementRepository;
    private final VideoRepository videoRepository;

    public VideoEngagementService(VideoEngagementRepository engagementRepository, VideoRepository videoRepository) {
        this.engagementRepository = engagementRepository;
        this.videoRepository = videoRepository;
    }

    @Transactional
    public void incrementViewCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));

        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> {
                    VideoEngagement newEngagement = new VideoEngagement();
                    newEngagement.setVideo(video);
                    return newEngagement;
                });

        engagement.incrementViews();
        engagementRepository.save(engagement);
    }

    @Transactional
    public void incrementLikeCount(Long videoId) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));

        VideoEngagement engagement = engagementRepository.findByVideoId(videoId)
                .orElseGet(() -> {
                    VideoEngagement newEngagement = new VideoEngagement();
                    newEngagement.setVideo(video);
                    return newEngagement;
                });

        engagement.incrementLikes();
        engagementRepository.save(engagement);
    }

    public VideoEngagement getEngagementStats(Long videoId) {
        return engagementRepository.findByVideoId(videoId)
                .orElseThrow(() -> new VideoNotFoundException("No engagement data found for this video"));
    }
}