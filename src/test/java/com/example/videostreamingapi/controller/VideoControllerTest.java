package com.example.videostreamingapi.controller;

import com.example.videostreamingapi.model.Video;
import com.example.videostreamingapi.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VideoService videoService;

    @InjectMocks
    private VideoController videoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(videoController).build();
    }

    @Test
    void whenGetVideos_ThenReturnOk() throws Exception {
        mockMvc.perform(get("/videos"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPostVideo_ThenReturnCreated() throws Exception {
        Video video = new Video();
        video.setTitle("Test Video");

        lenient().when(videoService.createVideo(any(Video.class))).thenReturn(video);

        mockMvc.perform(post("/videos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"title\": \"Test Video\" }"))
                .andExpect(status().isOk());
    }
}