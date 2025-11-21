package use_case.delete_entry;

import java.io.IOException;

public interface DeleteEntryUserDataAccessInterface {
    boolean existsByPath(String entryPath) throws IOException;
    void deleteByPath(String entryPath) throws IOException;
}
