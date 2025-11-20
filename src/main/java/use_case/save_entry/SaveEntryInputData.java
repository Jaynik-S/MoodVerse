package use_case.save_entry;

import entity.DiaryEntry;

public class SaveEntryInputData {
    private final DiaryEntry entry;

    public SaveEntryInputData(DiaryEntry entry) {
        this.entry = entry;
    }

    public DiaryEntry getEntry() {
        return entry;
    }
}

