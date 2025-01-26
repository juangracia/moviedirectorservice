package com.example.moviedirector.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.moviedirector.exception.GlobalExceptionHandler;
import com.example.moviedirector.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

public class DirectorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DirectorService directorService;

    @InjectMocks
    private DirectorController directorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(directorController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    public void getDirectorsWithValidThreshold() throws Exception {
        when(directorService.getDirectors(1)).thenReturn(Collections.singletonList("Woody Allen"));

        mockMvc.perform(get("/api/directors")
                .param("threshold", "1")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"directors\":[\"Woody Allen\"]}"));
    }

    @Test
    public void getDirectorsWithInvalidThreshold() throws Exception {
        mockMvc.perform(get("/api/directors")
                .param("threshold", "0")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Threshold must be greater than 0"));
    }
}