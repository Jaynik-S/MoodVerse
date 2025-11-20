package use_case.save_entry;

import entity.DiaryEntry;

public class SaveEntryOutputData {

    private final DiaryEntry entry;
    private final String entryPath;
    private final boolean success;
    public SaveEntryOutputData(DiaryEntry entry, String entryPath, boolean success) {
        this.entry = entry;
        this.entryPath = entryPath;
        this.success = success;
    }

    public DiaryEntry getEntry() {
        return entry;
    }
    public String getEntryPath() {
        return entryPath;
    }
    public boolean isSuccess() {
        return success;
    }
}

