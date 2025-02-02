package com.example.videostreamingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.videostreamingapi.entity.VideoEngagement;

import java.util.Optional;

public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, Long> {
    Optional<VideoEngagement> findByVideoId(Long videoId);
}