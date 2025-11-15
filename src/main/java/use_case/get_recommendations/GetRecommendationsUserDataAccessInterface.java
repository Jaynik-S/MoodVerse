package use_case.get_recommendations;

import entity.MovieRecommendation;
import entity.SongRecommendation;

import java.io.IOException;
import java.util.List;

public interface GetRecommendationsUserDataAccessInterface {
    List<String> fetchKeywords(String textBody);
    List<SongRecommendation> fetchSongRecommendations(List<String> keywords) throws IOException, InterruptedException;
    List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords) throws IOException, InterruptedException;

}
