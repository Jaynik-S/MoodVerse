package use_case.load_entry;

import java.io.IOException;

public interface LoadEntryOutputBoundary {
    void prepareSuccessView(LoadEntryInputData outputData);
    void prepareFailureView(String errorMessage);
}