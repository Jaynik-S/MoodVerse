package use_case.create_entry;

public interface CreateEntryOutputBoundary {
    void prepareSuccessView(CreateEntryOutputData outputData);
    void prepareFailView(String errorMessage);
}