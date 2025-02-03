package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.entity.Video;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoRepository;
import com.example.videostreamingapi.util.VideoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final VideoRepository videoRepository;
    private final VideoEngagementService engagementService;

    public VideoService(VideoRepository videoRepository, VideoEngagementService engagementServices) {
        this.videoRepository = videoRepository;
        this.engagementService = engagementServices;
    }

    public Page<VideoResponse> listVideos(Pageable pageable) {
        logger.info("Fetching all active videos...");
        return videoRepository.findAllByActiveTrue(pageable).map(VideoMapper::toResponse);
    }

    public Page<VideoResponse> searchVideos(String query, Pageable pageable) {
        logger.info("Searching videos with query: {}", query);
        return videoRepository.searchVideos(query, pageable).map(VideoMapper::toResponse);
    }

    @Transactional
    public VideoResponse createVideo(VideoRequest videoRequest) {
        logger.info("Creating new video: {} by {}", videoRequest.getTitle(), videoRequest.getDirector());

        return videoRepository.findByTitleAndDirector(videoRequest.getTitle(), videoRequest.getDirector())
                .map(existingVideo -> existingVideo.isActive()
                        ? VideoMapper.toResponse(existingVideo)
                        : VideoMapper.toResponse(reactivateVideo(existingVideo)))
                .orElseGet(() -> VideoMapper.toResponse(videoRepository.save(VideoMapper.toEntity(videoRequest))));
    }

    @Transactional
    public VideoResponse getVideoById(Long id) {
        logger.info("Fetching video with ID: {}", id);

        VideoResponse video = videoRepository.findByIdAndActiveTrue(id)
                .map(VideoMapper::toResponse)
                .orElseThrow(() -> new VideoNotFoundException("Active video not found with ID: " + id));

        engagementService.recordImpression(id);

        return video;
    }

    @Transactional
    public String playVideo(Long videoId) {
        VideoResponse video = getVideoById(videoId);
        engagementService.recordView(videoId);
        logger.info("Playing video: {}", video.getTitle());

        return "Streaming video: " + video.getTitle();
    }

    @Transactional
    public boolean delistVideo(Long id) {
        logger.info("Delisting video with ID: {}", id);
        return videoRepository.findById(id)
                .map(video -> {
                    if (!video.isActive()) {
                        logger.warn("Video with ID {} is already delisted", id);
                        return false;
                    }
                    video.setActive(false);
                    videoRepository.save(video);
                    logger.info("Video ID: {} has been delisted", id);
                    return true;
                })
                .orElseThrow(() -> new VideoNotFoundException("Video not found with ID: " + id));
    }

    @Transactional
    public VideoResponse updateMetadata(Long id, VideoRequest videoRequest) {
        logger.info("Updating metadata for video ID: {}", id);
        return videoRepository.findByIdAndActiveTrue(id)
                .map(video -> {
                    VideoMapper.updateMetadata(video, videoRequest);
                    Video updatedVideo = videoRepository.save(video);
                    logger.info("Metadata updated successfully for video ID: {}", updatedVideo.getId());
                    return VideoMapper.toResponse(updatedVideo);
                })
                .orElseThrow(() -> new VideoNotFoundException("Active video not found with ID: " + id));
    }

    private Video reactivateVideo(Video video) {
        logger.info("Reactivating video: {} by {}", video.getTitle(), video.getDirector());
        video.setActive(true);
        return videoRepository.save(video);
    }
}