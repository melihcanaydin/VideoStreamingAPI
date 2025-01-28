package com.example.videostreamingapi.model;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String director;
    private String genre;
    private Integer year;
    private Integer runningTime;
    private boolean active = true;
}
