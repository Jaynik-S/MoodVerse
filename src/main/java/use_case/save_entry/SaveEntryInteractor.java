package use_case.save_entry;

import entity.DiaryEntry;
import use_case.diary.DiaryEntryDataAccessInterface;

public class SaveEntryInteractor implements SaveEntryInputBoundary {

    private final SaveEntryOutputBoundary presenter;
    private final DiaryEntryDataAccessInterface dataAccess;

    public SaveEntryInteractor(SaveEntryOutputBoundary presenter, DiaryEntryDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(DiaryEntry entry) {

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

        entry.updatedTime();

        dataAccess.save(entry);

        presenter.prepareSuccessView(entry);
    }
}

