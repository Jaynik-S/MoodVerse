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

        DiaryEntry entry = new DiaryEntry(inputData.getTitle(), inputData.getTextBody(), inputData.getDate());

        String title = entry.getTitle();
        if (title == null || title.length() == 0) {
            presenter.prepareFailView("Title cannot be empty.");
            return;
        }
        if (title.length() > DiaryEntry.MAX_TITLE_LENGTH) {
            String message = "Title must be at most " + DiaryEntry.MAX_TITLE_LENGTH + " characters.";
            presenter.prepareFailView(message);
            return;
        }
        String text = entry.getText();
        if (text == null || text.length() == 0) {
            presenter.prepareFailView("Text cannot be empty.");
            return;
        }
        int length = text.length();
        if (length < DiaryEntry.MIN_TEXT_LENGTH) {
            String message = "Text must be at least " + DiaryEntry.MIN_TEXT_LENGTH + " characters.";
            presenter.prepareFailView(message);
            return;
        }
        if (length > DiaryEntry.MAX_TEXT_LENGTH) {
            String message = "Text must be at most " + DiaryEntry.MAX_TEXT_LENGTH + " characters.";
            presenter.prepareFailView(message);
            return;
        }

        entry.updatedTime();
        try {
            dataAccess.save(entry);
        }
        catch (Exception error) {
            String message = "Could not save entry." + error.getMessage();
            presenter.prepareFailView(message);
            return;
        }

        SaveEntryOutputData outputData = new SaveEntryOutputData(
                entry.getTitle(),
                entry.getText(),
                entry.getCreatedAt(),
                true);

        presenter.prepareSuccessView(outputData);
    }
}

