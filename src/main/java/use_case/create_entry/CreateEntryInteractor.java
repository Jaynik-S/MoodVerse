package use_case.create_entry;

import entity.DiaryEntry;

import java.util.ArrayList;
import java.util.List;


public class CreateEntryInteractor implements CreateEntryInputBoundary {
    private final CreateEntryOutputBoundary presenter;

    public CreateEntryInteractor(CreateEntryOutputBoundary presenter) { this.presenter = presenter; }

    @Override
    public void execute(CreateEntryInputData inputData) {
        String title = inputData.getTitle();
        String text = inputData.getText();
        List<String> keywords = inputData.getKeywords();

        if (title == null || title.length() == 0) {
            presenter.prepareFailView("Title cannot be empty.");
            return;
        }
        if (title.length() > DiaryEntry.MAX_TITLE_LENGTH) {
            String message = "Title must be at most " + DiaryEntry.MAX_TITLE_LENGTH + " characters.";
            presenter.prepareFailView(message);
            return;
        }
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

        DiaryEntry entry = new DiaryEntry();
        entry.setTitle(title);
        entry.setText(text);
        entry.updatedTime();

        if (keywords == null) {
            entry.setKeyword(new ArrayList<String>());
        } else {
            List<String> copy = new ArrayList<String>(keywords);
            entry.setKeyword(copy);
        }

        String preview;
        if (text.length() <= 60) {
            preview = text;
        } else {
            preview = text.substring(0, 57) + "...";
        }
        CreateEntryOutputData outputData =
                new CreateEntryOutputData(entry.getEntryId(),
                        entry.getTitle(),
                        preview,
                        entry.getCreatedAt());
        presenter.prepareSuccessView(outputData);
    }
}

