package data_access;

import entity.MovieRecommendation;
import entity.SongRecommendation;
import entity.Keyword;
import use_case.get_recommendations.GetRecommendationsUserDataAccessInterface;

import java.util.List;

public class RecommendationAPIAccessObject implements GetRecommendationsUserDataAccessInterface {

    private final NLPAnalysisDataAccessObject nlpAnalysisDataAccessObject;

    public RecommendationAPIAccessObject() {
        this(NLPAnalysisDataAccessObject.createWithDefaultPipeline());
    }

    public RecommendationAPIAccessObject(NLPAnalysisDataAccessObject nlpAnalysisDataAccessObject) {
        this.nlpAnalysisDataAccessObject = nlpAnalysisDataAccessObject;
    }

    @Override
    public List<String> fetchKeywords(String textBody) {
        return nlpAnalysisDataAccessObject.analyze(textBody)
                .keywords()
                .stream()
                .map(Keyword::text)
                .toList();
    }

    @Override
    public List<SongRecommendation> fetchSongRecommendations(List<String> keywords) throws Exception {
        try {
            SpotifyAPIAccessObject spotifyAPI = new SpotifyAPIAccessObject(keywords);
            return spotifyAPI.fetchSongRecommendations();
        }
        catch (Exception error) {
            throw new Exception("Error fetching song recommendations: " + error.getMessage());
        }
    }

    @Override
    public List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords) throws Exception {
        try {
            TMDbAPIAccessObject tmdbAPI = new TMDbAPIAccessObject(keywords);
            return tmdbAPI.fetchMovieRecommendations();
        }
        catch (Exception error) {
            throw new Exception("Error fetching movie recommendations: " + error.getMessage());
        }
    }

}
