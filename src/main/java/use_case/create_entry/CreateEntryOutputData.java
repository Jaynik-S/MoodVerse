package use_case.create_entry;

import java.time.LocalDateTime;

public class CreateEntryOutputData {
    private final int entryId;
    private final String title;
    private final String textPreview;
    private final LocalDateTime createdAt;

    public CreateEntryOutputData(int entryId, String title, String textPreview, LocalDateTime createdAt) {
        this.entryId = entryId;
        this.title = title;
        this.textPreview = textPreview;
        this.createdAt = createdAt;
    }
    public int getEntryId() { return entryId; }
    public String getTitle() { return title; }
    public String getTextPreview() { return textPreview; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

