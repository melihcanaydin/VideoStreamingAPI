package com.example.videostreamingapi.repository;

import com.example.videostreamingapi.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllByActiveTrue();

    @Query("SELECT v FROM Video v WHERE v.active = true AND " +
           "(v.title LIKE %:query% OR v.director LIKE %:query%)")
    List<Video> searchVideos(@Param("query") String query);
}
