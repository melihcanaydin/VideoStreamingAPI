package com.example.videostreamingapi.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoEngagementRequest {
    @Min(value = 0, message = "Views cannot be negative")
    private int views;

    @Min(value = 0, message = "Impressions cannot be negative")
    private int impressions;
}