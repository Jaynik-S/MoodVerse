package use_case.get_recommendations;

import entity.MovieRecommendation;
import entity.SongRecommendation;

import java.util.List;

public class GetRecommendationsOutputData {

    private List<String> keywords;
    private List<SongRecommendation> songRecommendations;
    private List<MovieRecommendation> MovieRecommendations;

    public GetRecommendationsOutputData(List<String> keywords,
                                        List<SongRecommendation> songRecommendations,
                                        List<MovieRecommendation> movieRecommendations) {
        this.keywords = keywords;
        this.songRecommendations = songRecommendations;
        MovieRecommendations = movieRecommendations;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public List<SongRecommendation> getSongRecommendations() {
        return songRecommendations;
    }

    public List<MovieRecommendation> getMovieRecommendations() {
        return MovieRecommendations;
    }
}

