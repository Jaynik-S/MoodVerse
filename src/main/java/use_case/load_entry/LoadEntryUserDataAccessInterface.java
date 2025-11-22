package use_case.load_entry;

import entity.DiaryEntry;

public interface LoadEntryUserDataAccessInterface {
    DiaryEntry getByPath(String entryPath) throws Exception;
}
