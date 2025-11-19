package use_case.save_entry;

import entity.DiaryEntry;

public interface SaveEntryInputBoundary {
    void execute(DiaryEntry entry);
}