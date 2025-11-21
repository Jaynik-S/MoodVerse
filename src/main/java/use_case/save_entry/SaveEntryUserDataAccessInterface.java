package use_case.save_entry;

import entity.DiaryEntry;

public interface SaveEntryUserDataAccessInterface {
    boolean save(DiaryEntry entry) throws Exception;
}

