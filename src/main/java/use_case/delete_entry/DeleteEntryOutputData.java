package use_case.delete_entry;

public class DeleteEntryOutputData {
    private final boolean success;

    public DeleteEntryOutputData(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}

