package entity;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryEntry {
    public static final String BASE_DIR = "src/main/java/data_access/diary_entry_database"; // EXCEPTION CHECK DIR EXISTS
    public static final int MAX_TITLE_LENGTH = 30;
    public static final int MIN_TEXT_LENGTH = 50;
    public static final int MAX_TEXT_LENGTH = 1000;
    private static int idGenerator = 0;

    private final int entryId;
    private String title;
    private String text;
    private List<String> keywords;
    private String storagePath;            // BASE_DIR + entryId + title ".json";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean saved;
    private List<SongRecommendation> songRecommendations;
    private List<MovieRecommendation> MovieRecommendations;

    public DiaryEntry() {
        this.entryId = idGenerator;
        idGenerator++;
        this.title = "";
        this.text = "";
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
        this.saved = false;

    }
    // MIGHT NEED TO OVERRIDE CONSTRUCTOR LATER (FOR LOAD METHOD)


    public int getEntryId() { return entryId; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public List<String> getKeyword() { return keywords; }
    public String getStoragePath() { return storagePath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<SongRecommendation> getRecommendations() { return songRecommendations; }
    public List<MovieRecommendation> getMovieRecommendations() { return MovieRecommendations; }

    public void setTitle(String title) { this.title = title; }
    public void setText(String text) { this.text = text; }
    public void setKeyword(List<String> keyword) { this.keywords = keyword; }
    public void setRecommendations(java.util.List<entity.SongRecommendation> songRecommendations) {
        this.songRecommendations = songRecommendations;
    }
    public void setMovieRecommendations(java.util.List<entity.MovieRecommendation> movieRecommendations) {
        this.MovieRecommendations = movieRecommendations;
    }
    public void updatedTime() { this.updatedAt = LocalDateTime.now(); }
    public void setStoragePath(String storagePath) { this.storagePath = storagePath; }


    // TODO: METHODS (SAVE TO JSON, LOAD JSON, ETC)

}
