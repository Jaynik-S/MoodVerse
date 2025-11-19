package use_case.diary;

import entity.DiaryEntry;
import java.util.List;
public interface DiaryEntryDataAccessInterface {
    void save(DiaryEntry entry);
    boolean existsById(int entryId);
    DiaryEntry getByID(int entryId);
    List<DiaryEntry> getAll();
    void deleteById(int entryId);
}