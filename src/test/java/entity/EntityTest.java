package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest {

    @Test
    public void testKeyword() {
        Keyword kw = new Keyword("joy", 0.9);
        assertEquals("joy", kw.text());
        assertEquals(0.9, kw.score(), 1e-9);
    }

    @Test
    public void testAnalysisResultImmutability() {
        Keyword kw1 = new Keyword("a", 0.5);
        Keyword kw2 = new Keyword("b", 0.7);
        AnalysisResult result = new AnalysisResult(List.of(kw1, kw2));

        assertEquals(2, result.keywords().size());
        assertThrows(UnsupportedOperationException.class, () -> result.keywords().add(kw1));
    }

    @Test
    public void testSongRecommendation() {
        SongRecommendation rec = new SongRecommendation("2020", "img", "song", "artist", "90/100", "url");
        assertEquals("2020", rec.getReleaseYear());
        assertEquals("img", rec.getImageUrl());
        assertEquals("song", rec.getSongName());
        assertEquals("artist", rec.getArtistName());
        assertEquals("90/100", rec.getPopularityScore());
        assertEquals("url", rec.getExternalUrl());
    }

    @Test
    public void testMovieRecommendation() {
        MovieRecommendation rec = new MovieRecommendation("2019", "img2", "title", "8/10", "nice");
        assertEquals("2019", rec.getReleaseYear());
        assertEquals("img2", rec.getImageUrl());
        assertEquals("title", rec.getMovieTitle());
        assertEquals("8/10", rec.getMovieRating());
        assertEquals("nice", rec.getOverview());
    }

    @Test
    public void testDiaryEntry() {
        DiaryEntry entry = new DiaryEntry();
        int id = entry.getEntryId();
        assertTrue(id >= 0);

        entry.setTitle("My Day");
        entry.setText("This is a long enough diary entry text to pass validation length.");
        entry.setKeywords(List.of("happy", "sunny"));

        assertEquals("My Day", entry.getTitle());
        assertEquals("This is a long enough diary entry text to pass validation length.", entry.getText());
        assertEquals(List.of("happy", "sunny"), entry.getKeywords());

        LocalDateTime created = entry.getCreatedAt();
        LocalDateTime updated1 = entry.getUpdatedAt();
        assertNotNull(created);
        assertEquals(created, updated1);

        entry.updatedTime();
        LocalDateTime updated2 = entry.getUpdatedAt();
        assertTrue(updated2.isAfter(created) || updated2.equals(created));
    }
}
