package entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class NoteEntry {
    private final int entryId;
    private String title;
    private String text;
    private String contentPreview;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> tags;

    private String primaryMood;
    private List<String> emotions;

    private List<songRecommendation> spotifyTracks;
    private List<movieRecommendation> movies;

    public NoteEntry(int entryId, String title, String text, String contentPreview, Instant createdAt,
                     Instant updatedAt, List<String> tags) {
        this.entryId = entryId;
        this.title = title;
        this.text = text;
        this.contentPreview = preview(text);
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
        this.primaryMood = "neutral";
        this.emotions = new ArrayList<>();
        this.spotifyTracks = new ArrayList<>();
        this.movies = new ArrayList<>();
        if (tags == null) {
            this.tags = new ArrayList<>();
        }
        else {
            this.tags = new ArrayList<>(tags);
        }
    }

    public void touch() {
        this.updatedAt = Instant.now();
        this.contentPreview = preview(text);
    }

    private static String preview(String text) {
        if (text == null) {
            return "";
        }
        else if (text.length() <= 60) {
            return text : text.substring(0, 57) + "...";
        }
    }
    public int getEntryId() {
        return entryId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text; touch();
    }
    public String getContentPreview() {
        return contentPreview;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public List<String> getTags() {
        return java.util.Collections.unmodifiableList(tags);
    }
    public void setTags(List<String> t) {
        this.tags = new ArrayList<>(t); touch();
    }

    public String getPrimaryMood() {
        return primaryMood;
    }
    public void setPrimaryMood(String primaryMood) {
        this.primaryMood = primaryMood;
    }
    public List<String> getEmotions() {
        return emotions;
    }
    public void setEmotions(List<String> emotions) {
        this.emotions = emotions;
    }

    public List<songRecommendation> getSpotifyTracks() {
        return spotifyTracks;
    }
    public void setSpotifyTracks(List<songRecommendation> spotifyTracks) {
        this.spotifyTracks = spotifyTracks;
    }
    public List<movieRecommendation> getMovies() {
        return movies;
    }
    public void setMovies(List<movieRecommendation> movies) {
        this.movies = movies;
    }
}
