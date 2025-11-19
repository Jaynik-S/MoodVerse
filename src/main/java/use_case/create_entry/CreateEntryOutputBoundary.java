package use_case.create_entry;

import entity.DiaryEntry;

public interface CreateEntryOutputBoundary {
    void prepareSuccessView(DiaryEntry newEntry);
    void prepareFailView(String errorMessage);
}