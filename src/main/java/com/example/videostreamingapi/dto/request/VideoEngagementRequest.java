package com.example.videostreamingapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoEngagementRequest {
    @Min(value = 0, message = "Views cannot be negative")
    @Schema(description = "Number of views to increment", example = "1")
    private int views;

    @Min(value = 0, message = "Impressions cannot be negative")
    @Schema(description = "Number of impressions to increment", example = "1")
    private int impressions;
}
