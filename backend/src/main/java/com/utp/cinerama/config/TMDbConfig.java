package com.utp.cinerama.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TMDbConfig {

    
    private final String apiKey = "a15638b6523e2921983beafd70d732f6";
    private final String baseUrl = "https://api.themoviedb.org/3";
    private final String language = "es-MX";
    private final String region = "PE";

    
    public String getNowPlayingUrl() {
        return baseUrl + "/movie/now_playing";
    }

    public String getPopularUrl() {
        return baseUrl + "/movie/popular";
    }

    public String getUpcomingUrl() {
        return baseUrl + "/movie/upcoming";
    }

    public String getMovieDetailsUrl(Long movieId) {
        return baseUrl + "/movie/" + movieId;
    }

    public String getGenresUrl() {
        return baseUrl + "/genre/movie/list";
    }
}

