package use_case.load_entry;

import entity.DiaryEntry;

public class LoadEntryInteractor implements LoadEntryInputBoundary{
    private final LoadEntryOutputBoundary presenter;
    private final LoadEntryUserDataAccessInterface dataAccess;

    public LoadEntryInteractor(LoadEntryOutputBoundary presenter, LoadEntryUserDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(LoadEntryInputData inputData) {
        String entryPath = inputData.getEntryPath();

        if (entryPath == null || entryPath.length() == 0) {
            presenter.prepareFailView("Entry path cannot be empty.");
            return;
        }
        DiaryEntry entry;
        try {
            entry = dataAccess.getByPath(entryPath);
        } catch (Exception e) {
            String message = "Failed to load entry: " + e.getMessage();
            presenter.prepareFailView(message);
            return;
        }
        if (entry == null) {
            String message = "Failed to load entry from path: " + entryPath;
            presenter.prepareFailView(message);
            return;
        }

        LoadEntryOutputData outputData = new LoadEntryOutputData(
                entry.getTitle(),
                entry.getText(),
                entry.getCreatedAt(),
                true);

        presenter.prepareSuccessView(outputData);
    }
}

