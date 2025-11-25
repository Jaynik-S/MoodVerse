package use_case.create_entry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CreateEntryInteractorTest {

    // Ensures executing the interactor creates a blank diary entry and notifies the presenter.
    @Test
    void execute_createsBlankEntryAndNotifiesPresenter() {
        RecordingCreateEntryPresenter presenter = new RecordingCreateEntryPresenter();
        CreateEntryInteractor interactor = new CreateEntryInteractor(presenter);

        interactor.execute();

        assertNotNull(presenter.successData);
        assertNull(presenter.errorMessage);
        assertEquals("Untitled Document", presenter.successData.getTitle());
        assertEquals("Enter your text here...", presenter.successData.getText());
        assertTrue(presenter.successData.isSuccess());
        assertNotNull(presenter.successData.getDate());
    }

    private static final class RecordingCreateEntryPresenter implements CreateEntryOutputBoundary {
        private CreateEntryOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(CreateEntryOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}
