package use_case.save_entry;

import entity.DiaryEntry;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SaveEntryInteractorTest {

    // Happy path: a valid entry should be persisted and surfaced on the success view.
    @Test
    void execute_withValidInput_savesEntryAndReturnsSuccess() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        SaveEntryInputData inputData = new SaveEntryInputData("Gratitude", LocalDateTime.now(), validText);

        interactor.execute(inputData);

        assertTrue(dataAccess.saveCalled);
        assertNotNull(dataAccess.savedEntry);
        assertNotNull(presenter.successData);
        assertNull(presenter.errorMessage);
        assertEquals("Gratitude", presenter.successData.getTitle());
        assertEquals(validText, presenter.successData.getText());
    }

    // Guards against saving entries that fail min length validation.
    @Test
    void execute_withShortText_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        SaveEntryInputData inputData = new SaveEntryInputData("Too Short", LocalDateTime.now(), "short text");

        interactor.execute(inputData);

        assertEquals("Text must be at least " + DiaryEntry.MIN_TEXT_LENGTH + " characters.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
        assertNull(dataAccess.savedEntry);
    }

    // Title must not be null or empty.
    @Test
    void execute_withEmptyTitle_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        SaveEntryInputData inputData = new SaveEntryInputData("", LocalDateTime.now(), validText);

        interactor.execute(inputData);

        assertEquals("Title cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    // Title must respect the MAX_TITLE_LENGTH constraint.
    @Test
    void execute_withTooLongTitle_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String longTitle = "a".repeat(DiaryEntry.MAX_TITLE_LENGTH + 1);
        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        SaveEntryInputData inputData = new SaveEntryInputData(longTitle, LocalDateTime.now(), validText);

        interactor.execute(inputData);

        assertEquals("Title must be at most " + DiaryEntry.MAX_TITLE_LENGTH + " characters.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    // Text must not be empty.
    @Test
    void execute_withEmptyText_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        SaveEntryInputData inputData = new SaveEntryInputData("Title", LocalDateTime.now(), "");

        interactor.execute(inputData);

        assertEquals("Text cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    // Text must not exceed MAX_TEXT_LENGTH.
    @Test
    void execute_withTooLongText_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String tooLongText = "a".repeat(DiaryEntry.MAX_TEXT_LENGTH + 1);
        SaveEntryInputData inputData = new SaveEntryInputData("Title", LocalDateTime.now(), tooLongText);

        interactor.execute(inputData);

        assertEquals("Text must be at most " + DiaryEntry.MAX_TEXT_LENGTH + " characters.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    @Test
    void saveEntryOutputData_exposesDate() {
        LocalDateTime now = LocalDateTime.now();
        SaveEntryOutputData data = new SaveEntryOutputData("title", "text", now, true);
        assertEquals(now, data.getDate());
    }

    @Test
    void execute_withNullTitle_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        SaveEntryInputData inputData = new SaveEntryInputData(null, LocalDateTime.now(), validText);

        interactor.execute(inputData);

        assertEquals("Title cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    @Test
    void execute_withNullText_reportsFailure() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        InMemorySaveEntryDataAccess dataAccess = new InMemorySaveEntryDataAccess();
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        SaveEntryInputData inputData = new SaveEntryInputData("Title", LocalDateTime.now(), null);

        interactor.execute(inputData);

        assertEquals("Text cannot be empty.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.saveCalled);
    }

    @Test
    void execute_whenSaveThrowsException_reportsFailureWithMessage() {
        RecordingSaveEntryPresenter presenter = new RecordingSaveEntryPresenter();
        SaveEntryUserDataAccessInterface dataAccess = entry -> { throw new RuntimeException("disk full"); };
        SaveEntryInteractor interactor = new SaveEntryInteractor(presenter, dataAccess);

        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        SaveEntryInputData inputData = new SaveEntryInputData("Title", LocalDateTime.now(), validText);

        interactor.execute(inputData);

        assertNotNull(presenter.errorMessage);
        assertTrue(presenter.errorMessage.startsWith("Could not save entry."));
        assertTrue(presenter.errorMessage.contains("disk full"));
        assertNull(presenter.successData);
    }

    private static final class RecordingSaveEntryPresenter implements SaveEntryOutputBoundary {
        private SaveEntryOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(SaveEntryOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static final class InMemorySaveEntryDataAccess implements SaveEntryUserDataAccessInterface {
        private boolean saveCalled;
        private DiaryEntry savedEntry;

        @Override
        public boolean save(DiaryEntry entry) {
            this.saveCalled = true;
            this.savedEntry = entry;
            return true;
        }
    }
}
