package use_case.delete_entry;

import java.io.IOException;

public class DeleteEntryInteractor implements DeleteEntryInputBoundary {

    private final DeleteEntryOutputBoundary presenter;
    private final DeleteEntryUserDataAccessInterface dataAccess;

    public DeleteEntryInteractor(DeleteEntryOutputBoundary presenter, DeleteEntryUserDataAccessInterface dataAccess) {
        this.presenter = presenter;
        this.dataAccess = dataAccess;
    }

    @Override
    public void execute(DeleteEntryInputData inputData) {
        String entryPath = inputData.getEntryPath();

        if (entryPath == null || entryPath.length() == 0) {
            presenter.prepareFailureView("Entry path cannot be empty.");
            return;
        }
        boolean exists;
        try {
            exists = dataAccess.existsByPath(entryPath);
        } catch (Exception e) {
            String message = "Failed to check entry existence: " + e.getMessage();
            presenter.prepareFailureView(message);
            return;
        }
        if (!exists) {
            String message = "No diary entry found at path: " + entryPath;
            presenter.prepareFailureView(message);
            return;
        }
        try {
            dataAccess.deleteByPath(entryPath);
        } catch (Exception e) {
            String message = "Failed to delete entry: " + e.getMessage();
            presenter.prepareFailureView(message);
            return;
        }
        DeleteEntryOutputData outputData = new DeleteEntryOutputData(true);

        presenter.prepareSuccessView(outputData);
    }

}

