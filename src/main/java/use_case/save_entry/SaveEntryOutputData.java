package use_case.save_entry;

import entity.DiaryEntry;

import java.time.LocalDateTime;

public class SaveEntryOutputData {
    private final String title;
    private final String text;
    private final LocalDateTime date;
    private final boolean success;
    public SaveEntryOutputData(String title, String text, LocalDateTime date, boolean success) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.success = success;
    }

    public String getTitle() {return title;}
    public String getText() {return text;}
    public LocalDateTime getDate() {return date;}
    public boolean isSuccess() {
        return success;
    }
}

