package com.example.videostreamingapi.service;

import com.example.videostreamingapi.exception.VideoNotFoundException;
import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoService.class);

    private final VideoRepository videoRepository;

    public VideoService(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public List<Video> listVideos() {
        logger.info("Fetching all active videos");
        List<Video> videos = videoRepository.findAllByActiveTrue();
        logger.info("Retrieved {} active videos", videos.size());
        return videos;
    }

    public Video publishVideo(Video video) {
        logger.info("Publishing new video: {}", video.getTitle());
        video.setActive(true);
        Video savedVideo = videoRepository.save(video);
        logger.info("Video published successfully with ID: {}", savedVideo.getId());
        return savedVideo;
    }

    public Video getVideoById(Long id) {
        logger.info("Fetching video with ID: {}", id);
        return videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Active video not found with ID: {}", id);
                    return new VideoNotFoundException("Active video not found with ID: " + id);
                });
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

    public void delistVideo(Long id) {
        logger.info("Delisting video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Video not found with ID: {}", id);
                    return new VideoNotFoundException("Video not found");
                });

        video.setActive(false);
        videoRepository.save(video);
        logger.info("Video ID: {} has been delisted", id);
    }

    public Video updateMetadata(Long id, Video updatedVideo) {
        logger.info("Updating metadata for video ID: {}", id);
        Video video = videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> {
                    logger.warn("Active video not found with ID: {}", id);
                    return new VideoNotFoundException("Active video not found");
                });

        video.setTitle(updatedVideo.getTitle());
        video.setDirector(updatedVideo.getDirector());
        video.setGenre(updatedVideo.getGenre());
        video.setYear(updatedVideo.getYear());

        Video updated = videoRepository.save(video);
        logger.info("Metadata updated successfully for video ID: {}", id);
        return updated;
    }
}