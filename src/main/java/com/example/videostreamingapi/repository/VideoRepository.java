package com.example.videostreamingapi.repository;

import com.example.videostreamingapi.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByActiveTrue();

    Optional<Video> findByIdAndActiveTrue(Long id);

    Optional<Video> findByTitleAndDirector(String title, String director);

    @Query("SELECT v FROM Video v WHERE v.active = true AND " +
            "(LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(v.director) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Video> searchVideos(@Param("query") String query);
}