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
        assertTrue(presenter.successData.isSuccess());
    }

    // Guards against saving entries that fail length validation.
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
