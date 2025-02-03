package com.example.videostreamingapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import com.example.videostreamingapi.entity.VideoEngagement;

public interface VideoEngagementRepository extends JpaRepository<VideoEngagement, Long> {

    Optional<VideoEngagement> findByVideoId(Long videoId);

    @Modifying
    @Transactional
    @Query("UPDATE VideoEngagement v SET v.views = v.views + 1 WHERE v.video.id = :videoId")
    int incrementViewCount(Long videoId);

    @Modifying
    @Transactional
    @Query("UPDATE VideoEngagement v SET v.impressions = v.impressions + 1 WHERE v.video.id = :videoId")
    int incrementImpressionCount(Long videoId);
}