package use_case.load_entry;

import entity.DiaryEntry;

import java.io.IOException;
import java.util.List;

public interface LoadEntryUserDataAccessInterface {
    boolean existsByPath(String entryPath) throws IOException;

    DiaryEntry getByPath(String entryPath) throws IOException;
    List<DiaryEntry> getAll() throws IOException;
}
