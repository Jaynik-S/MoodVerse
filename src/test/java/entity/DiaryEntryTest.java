package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DiaryEntryTest {

    @Test
    public void testDefaultConstructorInitializesFields() {
        DiaryEntry entry = new DiaryEntry();
        assertTrue(entry.getEntryId() >= 0);
        assertEquals("Untitled Document", entry.getTitle());
        assertEquals("Enter your text here...", entry.getText());
        assertNotNull(entry.getCreatedAt());
        assertEquals(entry.getCreatedAt(), entry.getUpdatedAt());
    }

    @Test
    public void testParameterizedConstructorInitializesFields() {
        LocalDateTime created = LocalDateTime.of(2024, 1, 1, 12, 0);
        DiaryEntry entry = new DiaryEntry("My Day", "Some body text", created);

        assertTrue(entry.getEntryId() >= 0);
        assertEquals("My Day", entry.getTitle());
        assertEquals("Some body text", entry.getText());
        assertEquals(created, entry.getCreatedAt());
        assertNotNull(entry.getUpdatedAt());
    }

    @Test
    public void testSettersAndGetters() {
        DiaryEntry entry = new DiaryEntry();
        entry.setTitle("Title");
        entry.setText("This is a long enough diary entry to be used for testing.");
        entry.setKeywords(List.of("a", "b"));

        assertEquals("Title", entry.getTitle());
        assertEquals("This is a long enough diary entry to be used for testing.", entry.getText());
        assertEquals(List.of("a", "b"), entry.getKeywords());
    }

    @Test
    public void testUpdatedTimeChangesUpdatedAt() throws InterruptedException {
        DiaryEntry entry = new DiaryEntry();
        LocalDateTime firstUpdated = entry.getUpdatedAt();
        Thread.sleep(5); // ensure time changes at millisecond resolution
        entry.updatedTime();
        LocalDateTime secondUpdated = entry.getUpdatedAt();
        assertFalse(secondUpdated.isBefore(firstUpdated));
    }

    @Test
    public void testGetStoragePathUsesBaseDirAndIdAndTitle() {
        DiaryEntry entry = new DiaryEntry();
        entry.setTitle("My / invalid : title?");
        String path = entry.getStoragePath();

        assertTrue(path.startsWith(DiaryEntry.BASE_DIR + "/"));
        assertTrue(path.endsWith(".json"));
        assertFalse(path.contains(":"));
        assertFalse(path.substring(path.indexOf(')') + 1).contains("/"));
        assertFalse(path.contains("?"));
    }

    @Test
    public void testRecommendationsListsCanBeSet() {
        DiaryEntry entry = new DiaryEntry();
        SongRecommendation song = new SongRecommendation("2020", "img", "song", "artist", "90", "url");
        MovieRecommendation movie = new MovieRecommendation("2019", "img2", "title", "8", "overview");

        entry.setRecommendations(List.of(song));
        entry.setMovieRecommendations(List.of(movie));

        assertEquals(1, entry.getRecommendations().size());
        assertEquals(song, entry.getRecommendations().get(0));
        assertEquals(1, entry.getMovieRecommendations().size());
        assertEquals(movie, entry.getMovieRecommendations().get(0));
    }
}
