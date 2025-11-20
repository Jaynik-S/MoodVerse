package use_case.save_entry;

import entity.DiaryEntry;

public class SaveEntryInputData {
    private final DiaryEntry entry;
    private final String entryPath;

    public SaveEntryInputData(DiaryEntry entry, String entryPath) {
        this.entry = entry;
        this.entryPath = entryPath;
    }

    public DiaryEntry getEntry() {
        return entry;
    }
    public String getEntryPath() {
        return entryPath;
    }
}

