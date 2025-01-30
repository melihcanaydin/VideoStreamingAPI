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

    public List<Video> getAllVideos() {
        logger.info("Fetching all videos");
        return videoRepository.findAllByActiveTrue();
    }

    public Video createVideo(Video video) {
        logger.info("Creating video with title: {}", video.getTitle());
        Video savedVideo = videoRepository.save(video);
        logger.debug("Video created successfully with ID: {}", savedVideo.getId());
        return savedVideo;
    }

    public Video getVideoById(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found with id: " + id));
    }

    public void deleteVideo(Long id) {
        logger.info("Deleting video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Video with ID: {} not found for deletion", id);
                    return new VideoNotFoundException("Video not found");
                });
        video.setActive(false);
        videoRepository.save(video);
        logger.info("Video with ID: {} soft-deleted successfully", id);
    }

    public Video updateVideo(Long id, Video updatedVideo) {
        logger.info("Updating video with ID: {}", id);
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Video with ID: {} not found for update", id);
                    return new VideoNotFoundException("Video not found");
                });

        video.setTitle(updatedVideo.getTitle());
        video.setDirector(updatedVideo.getDirector());
        video.setGenre(updatedVideo.getGenre());
        video.setYear(updatedVideo.getYear());
        logger.debug("Updating fields for video ID: {}", id);
        Video savedVideo = videoRepository.save(video);
        logger.info("Video with ID: {} updated successfully", id);
        return savedVideo;
    }
}