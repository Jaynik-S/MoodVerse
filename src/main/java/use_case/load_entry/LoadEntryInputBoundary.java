package use_case.load_entry;

import java.io.IOException;

public interface LoadEntryInputBoundary {
    void execute(LoadEntryInputData inputData) throws IOException;
}

