package com.example.moviedirector.controller;

import com.example.moviedirector.service.DirectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for handling director-related API endpoints.
 */
@RestController
@Slf4j
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    /**
     * Endpoint to fetch directors with movies exceeding a given threshold.
     * By default, the threshold is set to 0.
     *
     * @param threshold minimum number of movies a director must have to be included in the list
     *                  (default is 0)
     * @return list of directors with movies exceeding the threshold
     */
    @GetMapping("/api/directors")
    public Map<String, List<String>> getDirectors(@RequestParam(defaultValue = "0") int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("Threshold must be greater than 0");
        }
        log.info("Fetching directors with movies exceeding threshold: {}", threshold);
        return Collections.singletonMap("directors", directorService.getDirectors(threshold));
    }
}