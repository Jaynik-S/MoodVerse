package data_access;

import entity.MovieRecommendation;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TMDbAPIAccessObjectTest {

    @Test
    public void testJSONtoMovieRecommendation_AllFieldsPresent() {
        JSONObject movie = new JSONObject()
                .put("title", "Movie Title")
                .put("release_date", "2015-10-21")
                .put("vote_average", "9.5")
                .put("overview", "Great movie")
                .put("poster_path", "/poster.jpg");

        TMDbAPIAccessObject dao = new TMDbAPIAccessObject(List.of("adventure"));
        MovieRecommendation rec = dao.JSONtoMovieRecommendation(movie);

        assertEquals("2015", rec.getReleaseYear());
        assertEquals("https://image.tmdb.org/t/p/original/poster.jpg", rec.getImageUrl());
        assertEquals("Movie Title", rec.getMovieTitle());
        assertEquals("9.5/10", rec.getMovieRating());
        assertEquals("Great movie", rec.getOverview());
    }

    @Test
    public void testJSONtoMovieRecommendation_MissingOptionalFields() {
        // No poster path, empty release_date
        JSONObject movie = new JSONObject()
                .put("title", "Another Movie")
                .put("release_date", "")
                .put("vote_average", "7.0");

        TMDbAPIAccessObject dao = new TMDbAPIAccessObject(List.of("drama"));
        MovieRecommendation rec = dao.JSONtoMovieRecommendation(movie);

        assertEquals("—", rec.getReleaseYear());
        assertEquals("—", rec.getImageUrl());
        assertEquals("Another Movie", rec.getMovieTitle());
        assertEquals("7.0/10", rec.getMovieRating());
        assertEquals("", rec.getOverview());
    }

    @Test
    public void testFetchMovieRecommendations_RealAPI_WhenApiKeyPresent() throws Exception {
        String apiKey = System.getenv("TMDB_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            return;
        }

        TMDbAPIAccessObject dao = new TMDbAPIAccessObject(List.of("adventure", "friendship"));
        List<MovieRecommendation> recs = dao.fetchMovieRecommendations();

        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        for (MovieRecommendation rec : recs) {
            assertNotNull(rec.getMovieTitle());
            assertNotNull(rec.getReleaseYear());
            System.out.println(rec.getMovieTitle() + " (" + rec.getReleaseYear() + ")");
        }
    }
}
