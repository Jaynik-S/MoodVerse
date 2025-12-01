package use_case.load_entry;

import entity.DiaryEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoadEntryInteractorTest {

    // Verifies a valid path reaches the DAO and returns the loaded entry to the presenter.
    @Test
    void execute_withValidPath_returnsEntryToPresenter() throws Exception {
        RecordingLoadEntryPresenter presenter = new RecordingLoadEntryPresenter();
        StubLoadEntryDataAccess dataAccess = new StubLoadEntryDataAccess();
        DiaryEntry entry = new DiaryEntry();
        entry.setTitle("My Day");
        entry.setText("a".repeat(60));
        dataAccess.setEntryToReturn(entry);
        LoadEntryInteractor interactor = new LoadEntryInteractor(presenter, dataAccess);

        interactor.execute(new LoadEntryInputData("entries/1.json"));

        assertEquals("entries/1.json", dataAccess.requestedPath);
        assertTrue(dataAccess.getCalled);
        assertNotNull(presenter.successData);
        assertEquals("My Day", presenter.successData.getTitle());
        assertEquals(entry.getText(), presenter.successData.getText());
        assertNull(presenter.errorMessage);
    }

    // Ensures validation fails fast when the entry path is missing.
    @Test
    void execute_withEmptyPath_reportsFailure() {
        RecordingLoadEntryPresenter presenter = new RecordingLoadEntryPresenter();
        StubLoadEntryDataAccess dataAccess = new StubLoadEntryDataAccess();
        LoadEntryInteractor interactor = new LoadEntryInteractor(presenter, dataAccess);

        interactor.execute(new LoadEntryInputData(""));

        assertEquals("Entry path cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.getCalled);
    }

    // Ensures DAO exceptions are surfaced as a failure view.
    @Test
    void execute_whenDataAccessThrows_reportsFailure() {
        RecordingLoadEntryPresenter presenter = new RecordingLoadEntryPresenter();
        FailingLoadEntryDataAccess dataAccess = new FailingLoadEntryDataAccess();
        LoadEntryInteractor interactor = new LoadEntryInteractor(presenter, dataAccess);

        interactor.execute(new LoadEntryInputData("entries/2.json"));

        assertEquals("Failed to load entry: boom", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    // Ensures a null entry from DAO is treated as a failure.
    @Test
    void execute_whenDaoReturnsNull_reportsFailure() {
        RecordingLoadEntryPresenter presenter = new RecordingLoadEntryPresenter();
        StubLoadEntryDataAccess dataAccess = new StubLoadEntryDataAccess();
        dataAccess.setEntryToReturn(null);
        LoadEntryInteractor interactor = new LoadEntryInteractor(presenter, dataAccess);

        interactor.execute(new LoadEntryInputData("entries/3.json"));

        assertEquals("Failed to load entry from path: entries/3.json", presenter.errorMessage);
        assertNull(presenter.successData);
    }

    @Test
    void loadEntryOutputData_exposesDate() {
        LocalDateTime now = LocalDateTime.now();
        LoadEntryOutputData data = new LoadEntryOutputData("title", "text", now, true);
        assertEquals(now, data.getDate());
    }

    private static final class RecordingLoadEntryPresenter implements LoadEntryOutputBoundary {
        private LoadEntryOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(LoadEntryOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static class StubLoadEntryDataAccess implements LoadEntryUserDataAccessInterface {
        private DiaryEntry entryToReturn = new DiaryEntry();
        private boolean getCalled;
        private String requestedPath;

        @Override
        public DiaryEntry getByPath(String entryPath) {
            this.getCalled = true;
            this.requestedPath = entryPath;
            return entryToReturn;
        }

        void setEntryToReturn(DiaryEntry entryToReturn) {
            this.entryToReturn = entryToReturn;
        }
    }

    private static class FailingLoadEntryDataAccess implements LoadEntryUserDataAccessInterface {
        @Override
        public DiaryEntry getByPath(String entryPath) {
            throw new RuntimeException("boom");
        }
    }
}
