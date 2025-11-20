package use_case.delete_entry;

public interface DeleteEntryOutputBoundary {
    void prepareSuccessView(DeleteEntryOutputData outputData);
    void prepareFailureView(String errorMessage);
}