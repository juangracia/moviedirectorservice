package com.example.moviedirector.service;

import com.example.moviedirector.model.Movie;
import com.example.moviedirector.model.MovieResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class DirectorServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DirectorService directorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(directorService, "movieApiBaseUrl", "http://example.com");
    }

    @Test
    public void getDirectorsWithValidThreshold() {
        Movie movie1 = new Movie();
        movie1.setDirector("Woody Allen");
        Movie movie2 = new Movie();
        movie2.setDirector("Quentin Tarantino");

        MovieResponse response = new MovieResponse();
        response.setData(Arrays.asList(movie1, movie2));
        response.setTotalPages(1);

        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(response);

        List<String> directors = directorService.getDirectors(0);
        assertEquals(2, directors.size());
        assertEquals("Quentin Tarantino", directors.get(0));
        assertEquals("Woody Allen", directors.get(1));
    }

    @Test
    public void getDirectorsWithThreshold() {
        Movie movie1 = new Movie();
        movie1.setDirector("Woody Allen");
        Movie movie2 = new Movie();
        movie2.setDirector("Woody Allen");
        Movie movie3 = new Movie();
        movie3.setDirector("Quentin Tarantino");

        MovieResponse response = new MovieResponse();
        response.setData(Arrays.asList(movie1, movie2, movie3));
        response.setTotalPages(1);

        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(response);

        List<String> directors = directorService.getDirectors(1);
        assertEquals(1, directors.size());
        assertEquals("Woody Allen", directors.get(0));
    }

    @Test
    public void fetchAllMoviesNullResponse() {
        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(null);

        List<Movie> movies = directorService.fetchAllMovies();
        assertTrue(movies.isEmpty());
    }

    @Test
    public void fetchAllMoviesEmptyResponse() {
        MovieResponse response = new MovieResponse();
        response.setData(Arrays.asList());
        response.setTotalPages(1);

        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(response);

        List<Movie> movies = directorService.fetchAllMovies();
        assertTrue(movies.isEmpty());
    }

    @Test
    public void fetchAllMoviesWithMultiplePages() {
        Movie movie1 = new Movie();
        movie1.setDirector("Woody Allen");
        Movie movie2 = new Movie();
        movie2.setDirector("Quentin Tarantino");

        MovieResponse responsePage1 = new MovieResponse();
        responsePage1.setData(Arrays.asList(movie1));
        responsePage1.setTotalPages(2);

        MovieResponse responsePage2 = new MovieResponse();
        responsePage2.setData(Arrays.asList(movie2));
        responsePage2.setTotalPages(2);

        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(responsePage1);
        when(restTemplate.getForObject("http://example.com?page=2", MovieResponse.class)).thenReturn(responsePage2);

        List<Movie> movies = directorService.fetchAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Woody Allen", movies.get(0).getDirector());
        assertEquals("Quentin Tarantino", movies.get(1).getDirector());
    }

    @Test
    public void fetchAllMoviesWithCommunicationError() {
        when(restTemplate.getForObject("http://example.com?page=1", MovieResponse.class)).thenReturn(null);

        List<Movie> movies = directorService.fetchAllMovies();
        assertTrue(movies.isEmpty());
    }
}