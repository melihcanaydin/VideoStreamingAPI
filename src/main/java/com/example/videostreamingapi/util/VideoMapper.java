package com.example.videostreamingapi.util;

import com.example.videostreamingapi.dto.request.VideoRequest;
import com.example.videostreamingapi.dto.response.VideoResponse;
import com.example.videostreamingapi.entity.Video;

public class VideoMapper {

    private VideoMapper() {
    }

    public static Video toEntity(VideoRequest videoRequest) {
        return new Video(
                videoRequest.getTitle(),
                videoRequest.getDirector(),
                videoRequest.getMainActor(),
                videoRequest.getGenre(),
                videoRequest.getRunningTime());
    }

    public static VideoResponse toResponse(Video video) {
        return new VideoResponse(
                video.getId(),
                video.getTitle(),
                video.getDirector(),
                video.getMainActor(),
                video.getGenre(),
                video.getRunningTime());
    }

    public static void updateMetadata(Video video, VideoRequest request) {
        video.setTitle(request.getTitle());
        video.setDirector(request.getDirector());
        video.setMainActor(request.getMainActor());
        video.setGenre(request.getGenre());
        video.setRunningTime(request.getRunningTime());
    }
}