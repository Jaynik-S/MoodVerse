package use_case.save_entry;

import entity.DiaryEntry;

public class SaveEntryInteractor implements SaveEntryInputBoundary {

    private final SaveEntryOutputBoundary presenter;
    private final SaveEntryUserDataAccessInterface dataAccess;

    public SaveEntryInteractor(SaveEntryOutputBoundary presenter, SaveEntryUserDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(SaveEntryInputData inputData) {

        DiaryEntry entry = inputData.getEntry();

        if (entry == null) {
            presenter.prepareFailureView("Entry cannot be null.");
            return;
        }
        String title = entry.getTitle();
        if (title == null || title.length() == 0) {
            presenter.prepareFailureView("Title cannot be empty.");
            return;
        }
        if (title.length() > DiaryEntry.MAX_TITLE_LENGTH) {
            String message = "Title must be at most " + DiaryEntry.MAX_TITLE_LENGTH + " characters.";
            presenter.prepareFailureView(message);
            return;
        }
        String text = entry.getText();
        if (text == null || text.length() == 0) {
            presenter.prepareFailureView("Text cannot be empty.");
            return;
        }
        int length = text.length();
        if (length < DiaryEntry.MIN_TEXT_LENGTH) {
            String message = "Text must be at least " + DiaryEntry.MIN_TEXT_LENGTH + " characters.";
            presenter.prepareFailureView(message);
            return;
        }
        if (length > DiaryEntry.MAX_TEXT_LENGTH) {
            String message = "Text must be at most " + DiaryEntry.MAX_TEXT_LENGTH + " characters.";
            presenter.prepareFailureView(message);
            return;
        }

        String storagePath = entry.getStoragePath();

        if (storagePath == null || storagePath.length() == 0) {
            presenter.prepareFailureView("Could not determine storage path for entry.");
            return;
        }

        entry.updatedTime();

        dataAccess.save(entry);
        entry.setSaved(true);

        SaveEntryOutputData outputData = new SaveEntryOutputData(entry, true);

        presenter.prepareSuccessView(outputData);
    }
}

