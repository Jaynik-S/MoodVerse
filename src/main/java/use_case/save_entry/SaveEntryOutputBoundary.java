package use_case.save_entry;

public interface SaveEntryOutputBoundary {
    void prepareSuccessView(SaveEntryOutputData outputData);
    void prepareFailureView(String errorMessage);
}

