package com.example.videostreamingapi.service;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.entity.Video;
import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);
    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public Page<VideoResponse> listVideos(Pageable pageable) {
        logger.info("Fetching all active videos");
        return videoRepository.findAllByActiveTrue(pageable)
                .map(this::mapToResponse);
    }

    public Page<VideoResponse> searchVideos(String query, Pageable pageable) {
        return videoRepository.searchVideos(query, pageable)
                .map(this::mapToResponse);
    }

    public VideoResponse createVideo(VideoRequest videoRequest) {
        logger.info("Attempting to publish video: {} by {}", videoRequest.getTitle(), videoRequest.getDirector());

        return findExistingVideo(videoRequest.getTitle(), videoRequest.getDirector())
                .map(existingVideo -> {
                    if (!existingVideo.isActive()) {
                        return mapToResponse(reactivateVideo(existingVideo));
                    }
                    logger.warn("The video {} from {} is already published!", videoRequest.getTitle(),
                            videoRequest.getDirector());
                    throw new IllegalStateException(
                            "The video " + videoRequest.getTitle() + " from " + videoRequest.getDirector()
                                    + " is already published!");
                })
                .orElseGet(() -> mapToResponse(saveNewVideo(videoRequest)));
    }

    public VideoResponse getVideoById(Long id) {
        logger.info("Fetching video with ID: {}", id);
        Video video = videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Active video not found with ID: {}", id);
                    return new VideoNotFoundException("Active video not found with ID: " + id);
                });
        return mapToResponse(video);
    }

    public String playVideo(Long videoId) {
        logger.info("Attempting to play video with ID: {}", videoId);
        Video video = videoRepository.findByIdAndActiveTrue(videoId)
                .orElseThrow(() -> {
                    logger.warn("Active video not found with ID: {}", videoId);
                    return new VideoNotFoundException("Active video not found with ID: " + videoId);
                });

        logger.info("Video is being streamed: {}", video.getTitle());
        return "Streaming video: " + video.getTitle();
    }

    public boolean delistVideo(Long id) {
        logger.info("Delisting video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Video not found with ID: {}", id);
                    return new VideoNotFoundException("Video not found");
                });

        if (!video.isActive()) {
            logger.warn("Video with ID {} is already delisted", id);
            return false;
        }

        video.setActive(false);
        videoRepository.save(video);
        logger.info("Video ID: {} has been delisted", id);
        return true;
    }

    public VideoResponse updateMetadata(Long id, VideoRequest videoRequest) {
        logger.info("Updating metadata for video ID: {}", id);
        Video video = videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Active video not found with ID: {}", id);
                    return new VideoNotFoundException("Active video not found");
                });

        video.setTitle(videoRequest.getTitle());
        video.setDirector(videoRequest.getDirector());
        video.setMainActor(videoRequest.getMainActor());
        video.setGenre(videoRequest.getGenre());
        video.setRunningTime(videoRequest.getRunningTime());

        Video updated = videoRepository.save(video);
        logger.info("Metadata updated successfully for video ID: {}", id);
        return mapToResponse(updated);
    }

    private Optional<Video> findExistingVideo(String title, String director) {
        return videoRepository.findByTitleAndDirector(title, director);
    }

    private Video saveNewVideo(VideoRequest videoRequest) {
        Video video = new Video();
        video.setTitle(videoRequest.getTitle());
        video.setDirector(videoRequest.getDirector());
        video.setMainActor(videoRequest.getMainActor());
        video.setGenre(videoRequest.getGenre());
        video.setRunningTime(videoRequest.getRunningTime());
        video.setActive(true);

        Video savedVideo = videoRepository.save(video);
        logger.info("Video published successfully with ID: {}", savedVideo.getId());
        return savedVideo;
    }

    private Video reactivateVideo(Video video) {
        logger.info("Reactivating previously delisted video: {} by {}", video.getTitle(), video.getDirector());
        video.setActive(true);
        return videoRepository.save(video);
    }

    private VideoResponse mapToResponse(Video video) {
        VideoResponse response = new VideoResponse();
        response.setId(video.getId());
        response.setTitle(video.getTitle());
        response.setDirector(video.getDirector());
        response.setMainActor(video.getMainActor());
        response.setGenre(video.getGenre());
        response.setRunningTime(video.getRunningTime());
        return response;
    }
}