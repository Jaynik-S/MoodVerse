package use_case.load_entry;

import entity.DiaryEntry;
import java.util.List;

public interface LoadEntryUserDataAccessInterface {
    boolean existsByPath(String entryPath);

    DiaryEntry getByPath(String entryPath);
    List<DiaryEntry> getAll();
}
