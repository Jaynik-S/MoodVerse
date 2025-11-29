package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MovieRecommendationTest {

    @Test
    public void testMovieRecommendationStoresAllFields() {
        MovieRecommendation rec = new MovieRecommendation("2019", "img2", "title", "8/10", "nice");
        assertEquals("2019", rec.getReleaseYear());
        assertEquals("img2", rec.getImageUrl());
        assertEquals("title", rec.getMovieTitle());
        assertEquals("8/10", rec.getMovieRating());
        assertEquals("nice", rec.getOverview());
    }

    @Test
    public void testMovieRecommendationAllowsEmptyOverview() {
        MovieRecommendation rec = new MovieRecommendation("2000", "img", "t", "5", "");
        assertEquals("", rec.getOverview());
    }
}

