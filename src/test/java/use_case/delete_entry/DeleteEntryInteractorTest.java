package use_case.delete_entry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeleteEntryInteractorTest {

    // Confirms a valid path triggers data deletion and success notification.
    @Test
    void execute_withValidPath_deletesEntryAndNotifiesPresenter() {
        RecordingDeleteEntryPresenter presenter = new RecordingDeleteEntryPresenter();
        InMemoryDeleteEntryDataAccess dataAccess = new InMemoryDeleteEntryDataAccess();
        DeleteEntryInteractor interactor = new DeleteEntryInteractor(presenter, dataAccess);

        interactor.execute(new DeleteEntryInputData("entries/1.json"));

        assertTrue(presenter.successData.isSuccess());
        assertNull(presenter.errorMessage);
        assertEquals("entries/1.json", dataAccess.lastDeletedPath);
        assertTrue(dataAccess.deleteCalled);
    }

    // Validates that an empty path short-circuits and reports an error.
    @Test
    void execute_withEmptyPath_reportsFailureAndSkipsDeletion() {
        RecordingDeleteEntryPresenter presenter = new RecordingDeleteEntryPresenter();
        InMemoryDeleteEntryDataAccess dataAccess = new InMemoryDeleteEntryDataAccess();
        DeleteEntryInteractor interactor = new DeleteEntryInteractor(presenter, dataAccess);

        interactor.execute(new DeleteEntryInputData(""));

        assertEquals("Entry path cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.deleteCalled);
    }

    private static final class RecordingDeleteEntryPresenter implements DeleteEntryOutputBoundary {
        private DeleteEntryOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(DeleteEntryOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static final class InMemoryDeleteEntryDataAccess implements DeleteEntryUserDataAccessInterface {
        private String lastDeletedPath;
        private boolean deleteCalled;

        @Override
        public boolean deleteByPath(String entryPath) {
            this.deleteCalled = true;
            this.lastDeletedPath = entryPath;
            return true;
        }
    }
}
