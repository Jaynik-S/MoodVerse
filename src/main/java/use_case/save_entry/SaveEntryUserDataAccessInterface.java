package use_case.save_entry;

import entity.DiaryEntry;

import java.io.IOException;

public interface SaveEntryUserDataAccessInterface {
    void save(DiaryEntry entry) throws IOException, InterruptedException;
}

