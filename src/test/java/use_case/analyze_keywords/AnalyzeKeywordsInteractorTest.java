package use_case.analyze_keywords;

import entity.AnalysisResult;
import entity.Keyword;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnalyzeKeywordsInteractorTest {

    @Test
    void execute_withNullOrBlankText_reportsValidationFailures() {
        RecordingAnalyzeKeywordsPresenter presenter = new RecordingAnalyzeKeywordsPresenter();
        StubAnalyzeKeywordsDataAccess dataAccess = new StubAnalyzeKeywordsDataAccess();
        AnalyzeKeywordsInteractor interactor = new AnalyzeKeywordsInteractor(dataAccess, presenter);

        // null text
        interactor.execute(new AnalyzeKeywordsInputData(null));
        assertEquals("Add some text to analyze keywords.", presenter.errorMessage);

        // blank text (spaces)
        presenter.errorMessage = null;
        interactor.execute(new AnalyzeKeywordsInputData("   "));
        assertEquals("Add some text to analyze keywords.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.called);
    }

    @Test
    void execute_withTooShortOrTooLongText_reportsLengthFailures() {
        RecordingAnalyzeKeywordsPresenter presenter = new RecordingAnalyzeKeywordsPresenter();
        StubAnalyzeKeywordsDataAccess dataAccess = new StubAnalyzeKeywordsDataAccess();
        AnalyzeKeywordsInteractor interactor = new AnalyzeKeywordsInteractor(dataAccess, presenter);

        // too short
        interactor.execute(new AnalyzeKeywordsInputData("short text"));
        assertEquals("Diary entry is too short to extract keywords.", presenter.errorMessage);

        // too long
        presenter.errorMessage = null;
        String tooLong = "a".repeat(entity.DiaryEntry.MAX_TEXT_LENGTH + 1);
        interactor.execute(new AnalyzeKeywordsInputData(tooLong));
        assertEquals("Diary entry is too long to extract keywords.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(dataAccess.called);
    }

    @Test
    void execute_withValidText_returnsExtractedKeywordStrings() {
        RecordingAnalyzeKeywordsPresenter presenter = new RecordingAnalyzeKeywordsPresenter();
        StubAnalyzeKeywordsDataAccess dataAccess = new StubAnalyzeKeywordsDataAccess();
        AnalyzeKeywordsInteractor interactor = new AnalyzeKeywordsInteractor(dataAccess, presenter);

        String valid = "a".repeat(entity.DiaryEntry.MIN_TEXT_LENGTH);
        interactor.execute(new AnalyzeKeywordsInputData(valid));

        assertNull(presenter.errorMessage);
        assertNotNull(presenter.successData);
        assertTrue(dataAccess.called);
        assertEquals(List.of("calm", "focus"), presenter.successData.getKeywords());
        assertEquals(valid, dataAccess.lastTextBody);
    }

    @Test
    void execute_whenDataAccessThrows_reportsFailure() {
        RecordingAnalyzeKeywordsPresenter presenter = new RecordingAnalyzeKeywordsPresenter();
        FailingAnalyzeKeywordsDataAccess dataAccess = new FailingAnalyzeKeywordsDataAccess();
        AnalyzeKeywordsInteractor interactor = new AnalyzeKeywordsInteractor(dataAccess, presenter);

        String valid = "a".repeat(entity.DiaryEntry.MIN_TEXT_LENGTH);
        interactor.execute(new AnalyzeKeywordsInputData(valid));

        assertTrue(presenter.errorMessage.startsWith("Failed to analyze keywords: "));
        assertNull(presenter.successData);
    }

    private static final class RecordingAnalyzeKeywordsPresenter implements AnalyzeKeywordsOutputBoundary {
        private AnalyzeKeywordsOutputData successData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(AnalyzeKeywordsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    private static class StubAnalyzeKeywordsDataAccess implements AnalyzeKeywordsDataAccessInterface {
        private boolean called;
        private String lastTextBody;

        @Override
        public AnalysisResult analyze(String textBody) {
            this.called = true;
            this.lastTextBody = textBody;
            List<Keyword> keywords = List.of(new Keyword("calm", 0.9), new Keyword("focus", 0.8));
            return new AnalysisResult(keywords);
        }
    }

    private static class FailingAnalyzeKeywordsDataAccess implements AnalyzeKeywordsDataAccessInterface {
        @Override
        public AnalysisResult analyze(String textBody) {
            throw new RuntimeException("service down");
        }
    }
}

