package use_case.delete_entry;

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
            presenter.prepareFailView("Entry path cannot be empty.");
            return;
        }
        try {
            dataAccess.deleteByPath(entryPath);
        }
        catch (Exception error) {
            String message = "Failed to delete entry: " + error.getMessage();
            presenter.prepareFailView(message);
            return;
        }
        DeleteEntryOutputData outputData = new DeleteEntryOutputData(true);

        presenter.prepareSuccessView(outputData);
    }

}

