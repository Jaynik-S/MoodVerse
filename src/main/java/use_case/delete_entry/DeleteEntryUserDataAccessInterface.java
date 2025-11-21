package use_case.delete_entry;

import java.io.IOException;

public interface DeleteEntryUserDataAccessInterface {
    boolean deleteByPath(String entryPath);
}
