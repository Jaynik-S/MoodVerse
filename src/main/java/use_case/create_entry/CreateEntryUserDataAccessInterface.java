package use_case.create_entry;

import entity.DiaryEntry;

public interface CreateEntryUserDataAccessInterface {
    void save(DiaryEntry entry);

    boolean existsById(int entryId);
}
