package use_case.save_entry;

import java.time.LocalDateTime;

public class SaveEntryInputData {
    private final String title;
    private final LocalDateTime date;
    private final String textBody;

    public SaveEntryInputData(String title, LocalDateTime date, String textBody) {
        this.title = title;
        this.date = date;
        this.textBody = textBody;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getTextBody() {
        return textBody;
    }

}

