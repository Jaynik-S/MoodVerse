package use_case.create_entry;

import entity.DiaryEntry;
import use_case.load_entry.LoadEntryOutputData;


public class CreateEntryInteractor implements CreateEntryInputBoundary {
    private final CreateEntryOutputBoundary presenter;

    public CreateEntryInteractor(CreateEntryOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        DiaryEntry entry = new DiaryEntry();
        CreateEntryOutputData outputData = new CreateEntryOutputData(
                entry.getTitle(),
                entry.getText(),
                entry.getCreatedAt(),
                true
        );

        presenter.prepareSuccessView(outputData);
    }
}

