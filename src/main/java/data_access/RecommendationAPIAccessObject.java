package data_access;

import entity.MovieRecommendation;
import entity.SongRecommendation;
import use_case.get_recommendations.GetRecommendationsUserDataAccessInterface;

import java.io.IOException;
import java.util.List;

public class RecommendationAPIAccessObject implements GetRecommendationsUserDataAccessInterface {

    @Override
    public List<String> fetchKeywords(String textBody) {
        // TODO: IMPLEMENT WITH EXTERNAL API (NLP)

        return List.of();
    }

    @Override
    public List<SongRecommendation> fetchSongRecommendations(List<String> keywords) throws IOException, InterruptedException {
        SpotifyAPIAccessObject spotifyAPI = new SpotifyAPIAccessObject(keywords);
        return spotifyAPI.fetchSongRecommendations();
    }

    @Override
    public List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords) throws IOException, InterruptedException {
        TMDbAPIAccessObject tmdbAPI = new TMDbAPIAccessObject(keywords);
        return tmdbAPI.fetchMovieRecommendations();
    }
}
