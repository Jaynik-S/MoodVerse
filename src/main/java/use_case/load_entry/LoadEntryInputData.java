package use_case.load_entry;

public class LoadEntryInputData {

    private final String entryPath;

    public LoadEntryInputData(String entryPath) {
        this.entryPath = entryPath;
    }

    public String getEntryPath() {
        return entryPath;
    }
}

