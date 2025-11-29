package use_case.go_back;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GoBackInteractorTest {

    // Gives confidence that the interactor simply forwards control to the presenter.
    @Test
    void execute_delegatesToPresenter() {
        RecordingGoBackPresenter presenter = new RecordingGoBackPresenter();
        GoBackInteractor interactor = new GoBackInteractor(presenter);

        interactor.execute();

        assertTrue(presenter.called);
    }

    private static final class RecordingGoBackPresenter implements GoBackOutputBoundary {
        private boolean called;

        @Override
        public void prepareSuccessView() {
            this.called = true;
        }
    }
}
