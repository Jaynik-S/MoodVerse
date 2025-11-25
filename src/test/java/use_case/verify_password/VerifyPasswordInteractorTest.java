package use_case.verify_password;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class VerifyPasswordInteractorTest {

    // Happy path confirming a valid password unlocks entries and informs the presenter.
    @Test
    void execute_withValidPassword_rendersEntriesAndReturnsSuccess() throws Exception {
        RecordingVerifyPasswordPresenter presenter = new RecordingVerifyPasswordPresenter();
        StubVerifyPasswordDataAccess verifyAccess = new StubVerifyPasswordDataAccess();
        StubRenderEntriesDataAccess renderAccess = new StubRenderEntriesDataAccess();
        renderAccess.entriesToReturn = List.of(Map.of("title", "Entry"));
        verifyAccess.passwordStatusToReturn = "Unlocked";
        VerifyPasswordInteractor interactor = new VerifyPasswordInteractor(verifyAccess, presenter, renderAccess);

        interactor.execute(new VerifyPasswordInputData("secret"));

        assertEquals("secret", verifyAccess.lastPassword);
        assertTrue(renderAccess.called);
        assertNotNull(presenter.successData);
        assertNull(presenter.errorMessage);
        assertEquals("Unlocked", presenter.successData.passwordStatus());
        assertEquals(renderAccess.entriesToReturn, presenter.successData.getAllEntries());
    }

    // Error path ensuring incorrect passwords raise a failure and skip entry loading.
    @Test
    void execute_withIncorrectPassword_reportsFailureWithoutLoadingEntries() throws Exception {
        RecordingVerifyPasswordPresenter presenter = new RecordingVerifyPasswordPresenter();
        StubVerifyPasswordDataAccess verifyAccess = new StubVerifyPasswordDataAccess();
        verifyAccess.passwordStatusToReturn = "Incorrect Password";
        StubRenderEntriesDataAccess renderAccess = new StubRenderEntriesDataAccess();
        VerifyPasswordInteractor interactor = new VerifyPasswordInteractor(verifyAccess, presenter, renderAccess);

        interactor.execute(new VerifyPasswordInputData("wrong"));

        assertEquals("Incorrect Password", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(renderAccess.called);
    }

    private static final class RecordingVerifyPasswordPresenter implements VerifyPasswordOutputBoundary {
        private VerifyPasswordOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(VerifyPasswordOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static final class StubVerifyPasswordDataAccess implements VerifyPasswordUserDataAccessInterface {
        private String passwordStatusToReturn = "Unlocked";
        private String lastPassword;

        @Override
        public String verifyPassword(String password) {
            this.lastPassword = password;
            return passwordStatusToReturn;
        }
    }

    private static final class StubRenderEntriesDataAccess implements RenderEntriesUserDataInterface {
        private List<Map<String, Object>> entriesToReturn = List.of();
        private boolean called;

        @Override
        public List<Map<String, Object>> getAll() {
            this.called = true;
            return entriesToReturn;
        }
    }
}
