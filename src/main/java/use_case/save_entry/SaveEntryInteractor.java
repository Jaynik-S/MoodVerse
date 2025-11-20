package use_case.save_entry;

import entity.DiaryEntry;
import use_case.save_entry.SaveEntryUserDataAccessInterface;

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
        String entryPath = inputData.getEntryPath();

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

        entry.setStoragePath(entryPath);
        entry.updatedTime();

        dataAccess.save(entry);
        entry.setSaved(true);

        SaveEntryOutputData outputData = new SaveEntryOutputData(entry, entryPath, true);

        presenter.prepareSuccessView(outputData);
    }
}

