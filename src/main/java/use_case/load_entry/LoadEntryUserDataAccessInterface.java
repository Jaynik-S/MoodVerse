package use_case.load_entry;

import entity.DiaryEntry;

import java.io.IOException;
import java.util.List;

public interface LoadEntryUserDataAccessInterface {
    DiaryEntry getByPath(String entryPath) throws IOException;
}
