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
        return videoRepository.findAllByActiveTrue();
    }

    public Video publishVideo(Video video) {
        video.setActive(true);
        return videoRepository.save(video);
    }

    public Video getVideoById(Long id) {
        return videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new VideoNotFoundException("Active video not found with ID: " + id));
    }

    public String playVideo(Long videoId) {
        Video video = videoRepository.findByIdAndActiveTrue(videoId)
                .orElseThrow(() -> new VideoNotFoundException("Active video not found with ID: " + videoId));

        return "Streaming video: " + video.getTitle();
    }

    public void delistVideo(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new VideoNotFoundException("Video not found"));

        video.setActive(false);
        videoRepository.save(video);
    }

    public Video updateMetadata(Long id, Video updatedVideo) {
        Video video = videoRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new VideoNotFoundException("Active video not found"));

        video.setTitle(updatedVideo.getTitle());
        video.setDirector(updatedVideo.getDirector());
        video.setGenre(updatedVideo.getGenre());
        video.setYear(updatedVideo.getYear());

        return videoRepository.save(video);
    }
}