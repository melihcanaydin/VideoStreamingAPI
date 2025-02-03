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
    @Query(value = """
            INSERT INTO video_engagement (video_id, views, impressions)
            VALUES (:videoId, 1, 0)
            ON CONFLICT (video_id)
            DO UPDATE SET views = video_engagement.views + 1
            """, nativeQuery = true)
    void upsertView(Long videoId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO video_engagement (video_id, views, impressions)
            VALUES (:videoId, 0, 1)
            ON CONFLICT (video_id)
            DO UPDATE SET impressions = video_engagement.impressions + 1
            """, nativeQuery = true)
    void upsertImpression(Long videoId);
}