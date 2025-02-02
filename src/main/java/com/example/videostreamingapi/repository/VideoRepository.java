package com.example.videostreamingapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.videostreamingapi.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    Page<Video> findAllByActiveTrue(Pageable pageable);

    Optional<Video> findByIdAndActiveTrue(Long id);

    Optional<Video> findByTitleAndDirector(String title, String director);

    @Query("SELECT v FROM Video v WHERE v.active = true AND " +
            "(LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(v.director) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(v.genre) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Video> searchVideos(@Param("query") String query, Pageable pageable);
}