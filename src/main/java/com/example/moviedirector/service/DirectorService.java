package com.example.moviedirector.service;

import com.example.moviedirector.model.Movie;
import com.example.moviedirector.model.MovieResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer for handling director-related business logic.
 */
@Service
@Slf4j
public class DirectorService {

    @Value("${movie-api.base-url}")
    private String movieApiBaseUrl;

    private final RestTemplate restTemplate;

    public DirectorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Fetches directors with a movie count strictly greater than the given threshold.
     *
     * @param threshold the minimum number of movies a director must have directed
     * @return a list of director names in alphabetical order
     */
    public List<String> getDirectors(int threshold) {
        List<Movie> allMovies = fetchAllMovies();

        // Count movies by director
        Map<String, Long> directorCounts = allMovies.stream()
            .collect(Collectors.groupingBy(Movie::getDirector, Collectors.counting()));

        // Filter and sort directors by threshold
        return directorCounts.entrySet().stream()
            .filter(entry -> entry.getValue() > threshold)
            .map(Map.Entry::getKey)
            .sorted()
            .collect(Collectors.toList());
    }

    /**
     * Fetches all movie records by iterating through API pages.
     *
     * @return a list of movies
     */
    public List<Movie> fetchAllMovies() {
        List<Movie> movies = new ArrayList<>();
        int page = 1;

        while (true) {
            String url = movieApiBaseUrl + "?page=" + page;
            MovieResponse response = restTemplate.getForObject(url, MovieResponse.class);
            if (response == null || response.getData() == null || response.getData().isEmpty()) {
                // log communication error
                log.error("Failed to fetch movies from " + url);
                break;
            }

            log.info("Fetched {} movies from page {}", response.getData().size(), page);
            log.debug("Movies: {}", response.getData());

            movies.addAll(response.getData());

            // Stop if the current page is the last page
            if (page >= response.getTotalPages()) {
                break;
            }
            page++;
        }

        return movies;
    }
}
