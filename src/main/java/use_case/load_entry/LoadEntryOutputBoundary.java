package use_case.load_entry;

public interface LoadEntryOutputBoundary {
    void prepareSuccessView(LoadEntryOutputData outputData);

    void prepareFailView(String errorMessage);
}