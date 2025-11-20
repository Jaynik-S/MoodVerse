package use_case.save_entry;

import entity.DiaryEntry;

public class SaveEntryOutputData {

    private final DiaryEntry entry;
    private final boolean success;
    public SaveEntryOutputData(DiaryEntry entry, boolean success) {
        this.entry = entry;
        this.success = success;
    }

    public DiaryEntry getEntry() {
        return entry;
    }
    public boolean isSuccess() {
        return success;
    }
}

