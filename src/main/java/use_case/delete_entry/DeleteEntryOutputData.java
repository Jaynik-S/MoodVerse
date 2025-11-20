package use_case.delete_entry;

public class DeleteEntryOutputData {
    private final String entryPath;
    private final boolean success;

    public DeleteEntryOutputData(String entryPath, boolean success) {
        this.entryPath = entryPath;
        this.success = success;
    }
    public String getEntryPath() { return entryPath; }
    public boolean isSuccess() { return success; }
}

