package use_case.create_entry;

import entity.DiaryEntry;

public interface CreateEntryOutputBoundary {
    void prepareSuccessView(CreateEntryOutputData outputData);
    void prepareFailView(String errorMessage);
}