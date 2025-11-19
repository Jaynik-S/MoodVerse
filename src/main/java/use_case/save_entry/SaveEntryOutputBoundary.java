package use_case.save_entry;

import entity.DiaryEntry;

public interface SaveEntryOutputBoundary {
    void prepareSuccessView(DiaryEntry savedEntry);
    void prepareFailureView(String errorMessage);
}

