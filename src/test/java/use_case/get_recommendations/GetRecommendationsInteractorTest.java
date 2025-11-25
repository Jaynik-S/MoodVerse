package use_case.get_recommendations;

import entity.DiaryEntry;
import entity.MovieRecommendation;
import entity.SongRecommendation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GetRecommendationsInteractorTest {

    // Covers the happy path where keywords and recs are fetched and UI transition occurs.
    @Test
    void execute_withValidText_fetchesRecommendationsAndSwitchesView() {
        RecordingRecommendationsPresenter presenter = new RecordingRecommendationsPresenter();
        StubRecommendationsDataAccess dataAccess = new StubRecommendationsDataAccess();
        GetRecommendationsInteractor interactor = new GetRecommendationsInteractor(dataAccess, presenter);

        String validText = "a".repeat(DiaryEntry.MIN_TEXT_LENGTH);
        interactor.execute(new GetRecommendationsInputData(validText));

        assertNotNull(presenter.successData);
        assertNull(presenter.errorMessage);
        assertTrue(presenter.switchedToMenu);
        assertEquals(dataAccess.keywordsToReturn, presenter.successData.getKeywords());
        assertEquals(dataAccess.songRecommendationsToReturn, presenter.successData.getSongRecommendations());
        assertEquals(dataAccess.movieRecommendationsToReturn, presenter.successData.getMovieRecommendations());
        assertEquals(validText, dataAccess.lastTextBody);
        assertTrue(dataAccess.fetchKeywordsCalled);
        assertTrue(dataAccess.fetchSongsCalled);
        assertTrue(dataAccess.fetchMoviesCalled);
    }

    // Ensures short diary entries fail fast before hitting the data access layer.
    @Test
    void execute_withShortText_reportsValidationFailure() {
        RecordingRecommendationsPresenter presenter = new RecordingRecommendationsPresenter();
        StubRecommendationsDataAccess dataAccess = new StubRecommendationsDataAccess();
        GetRecommendationsInteractor interactor = new GetRecommendationsInteractor(dataAccess, presenter);

        interactor.execute(new GetRecommendationsInputData("too short"));

        assertEquals("Diary entry is too short to extract recommendations.", presenter.errorMessage);
        assertNull(presenter.successData);
        assertFalse(presenter.switchedToMenu);
        assertFalse(dataAccess.fetchKeywordsCalled);
        assertFalse(dataAccess.fetchSongsCalled);
        assertFalse(dataAccess.fetchMoviesCalled);
    }

    private static final class RecordingRecommendationsPresenter implements GetRecommendationsOutputBoundary {
        private GetRecommendationsOutputData successData;
        private String errorMessage;
        private boolean switchedToMenu;

        @Override
        public void prepareSuccessView(GetRecommendationsOutputData outputData) {
            this.successData = outputData;
        }

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
        }

        @Override
        public void switchToRecommendationMenu() {
            this.switchedToMenu = true;
        }
    }

    private static final class StubRecommendationsDataAccess implements GetRecommendationsUserDataAccessInterface {
        private final List<String> keywordsToReturn = List.of("calm", "focus");
        private final List<SongRecommendation> songRecommendationsToReturn =
                List.of(new SongRecommendation("2020", "img", "song", "artist", "90", "url"));
        private final List<MovieRecommendation> movieRecommendationsToReturn =
                List.of(new MovieRecommendation("2021", "poster", "title", "8/10", "summary"));
        private boolean fetchKeywordsCalled;
        private boolean fetchSongsCalled;
        private boolean fetchMoviesCalled;
        private String lastTextBody;

        @Override
        public List<String> fetchKeywords(String textBody) {
            this.fetchKeywordsCalled = true;
            this.lastTextBody = textBody;
            return keywordsToReturn;
        }

        @Override
        public List<SongRecommendation> fetchSongRecommendations(List<String> keywords) {
            this.fetchSongsCalled = true;
            return songRecommendationsToReturn;
        }

        @Override
        public List<MovieRecommendation> fetchMovieRecommendations(List<String> keywords) {
            this.fetchMoviesCalled = true;
            return movieRecommendationsToReturn;
        }
    }
}
