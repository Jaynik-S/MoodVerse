package z_temp_files.entities;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryEntry {
    private int entryId;
    private String title;
    private String text;
    private String moodTag;
    private String keyword;
    private String storagePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RecommendationItem> recommendations;

    public int getEntryId() { return entryId; }
    public String getTitle() { return title; }
    public String getText() { return text; }
    public String getMoodTag() { return moodTag; }
    public String getKeyword() { return keyword; }
    public String getStoragePath() { return storagePath; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<RecommendationItem> getRecommendations() { return recommendations; }
}
