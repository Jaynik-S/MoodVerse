package use_case.save_entry;

import entity.DiaryEntry;

import java.io.IOException;

public interface SaveEntryUserDataAccessInterface {
    boolean save(DiaryEntry entry) throws IOException, InterruptedException;
}

