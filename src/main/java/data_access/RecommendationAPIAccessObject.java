package data_access;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import entity.MovieRecommendation;
import entity.SongRecommendation;
import entity.Keyword;
import use_case.get_recommendations.GetRecommendationsUserDataAccessInterface;

import java.util.List;
import java.util.Properties;

public class RecommendationAPIAccessObject implements GetRecommendationsUserDataAccessInterface {

    private final NLPAnalysisDataAccessObject nlpAnalysisDataAccessObject;

    public RecommendationAPIAccessObject() {
        this(createDefaultNlpDao());
    }

    public RecommendationAPIAccessObject(NLPAnalysisDataAccessObject nlpAnalysisDataAccessObject) {
        this.nlpAnalysisDataAccessObject = nlpAnalysisDataAccessObject;
    }

    private static NLPAnalysisDataAccessObject createDefaultNlpDao() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        return new NLPAnalysisDataAccessObject(pipeline);
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
        } catch (Exception e) {
            throw new Exception("Error fetching song recommendations: " + e.getMessage());
        }
    }

    @Override
    public List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords) throws Exception {
        try {
        TMDbAPIAccessObject tmdbAPI = new TMDbAPIAccessObject(keywords);
        return tmdbAPI.fetchMovieRecommendations();
    } catch (Exception e) {
        throw new Exception("Error fetching movie recommendations: " + e.getMessage());
    }
    }
}
