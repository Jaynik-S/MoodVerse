package use_case.get_recommendations;

import entity.MovieRecommendation;
import entity.SongRecommendation;

import java.util.List;

public interface GetRecommendationsUserDataAccessInterface {
    List<String> fetchKeywords(String textBody);
    List<SongRecommendation> fetchSongRecommendations(List<String> keywords);
    List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords);

}
