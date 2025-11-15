package use_case.get_recommendations;
import entity.DiaryEntry;
import entity.SongRecommendation;
import entity.MovieRecommendation;

import java.util.List;

public class GetRecommendationsInteractor implements  GetRecommendationsInputBoundary {
    private final GetRecommendationsUserDataAccessInterface userDataAccessObject;
    private final GetRecommendationsOutputBoundary recommendationPresenter;

    public GetRecommendationsInteractor(GetRecommendationsUserDataAccessInterface userDataAccessInterface,
                                        GetRecommendationsOutputBoundary getRecommendationsOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.recommendationPresenter = getRecommendationsOutputBoundary;
    }

    @Override
    public void execute(GetRecommendationsInputData inputData) {
        String textBody = inputData.getTextBody();
        if (textBody.length() < DiaryEntry.MIN_TEXT_LENGTH) {
            recommendationPresenter.prepareFailView("Diary entry is too short to extract recommendations.");
        }
        else if (textBody.length() > DiaryEntry.MAX_TEXT_LENGTH) {
            recommendationPresenter.prepareFailView("Diary entry is too long to extract recommendations.");
        }
        else {
            try {
                List<String> keywords = userDataAccessObject.fetchKeywords(textBody);
                List<SongRecommendation> songRecommendations = userDataAccessObject.fetchSongRecommendations(keywords);
                List<MovieRecommendation> movieRecommendations = userDataAccessObject.fetchMovieRecommendations(keywords);
                GetRecommendationsOutputData outputData = new GetRecommendationsOutputData(keywords, songRecommendations, movieRecommendations);
                recommendationPresenter.prepareSuccessView(outputData);

            } catch (Exception e) {
                recommendationPresenter.prepareFailView("Failed to get recommendations: " + e.getMessage());
            }
        }
    }

    @Override
    public void switchToRecommendationMenu() {
        recommendationPresenter.switchToRecommendationMenu();
    }

}