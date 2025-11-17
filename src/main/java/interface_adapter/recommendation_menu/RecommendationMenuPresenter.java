package interface_adapter.recommendation_menu;

import use_case.get_recommendations.GetRecommendationsOutputBoundary;
import use_case.get_recommendations.GetRecommendationsOutputData;

import java.util.ArrayList;

public class RecommendationMenuPresenter implements GetRecommendationsOutputBoundary {

    private final RecommendationMenuViewModel recommendationMenuViewModel;

    public RecommendationMenuPresenter(RecommendationMenuViewModel recommendationMenuViewModel) {
        this.recommendationMenuViewModel = recommendationMenuViewModel;
    }


    @Override
    public void prepareSuccessView(GetRecommendationsOutputData outputData) {
        final RecommendationMenuState recommendationMenuState = recommendationMenuViewModel.getState();
        recommendationMenuState.setSongRecommendation(outputData.getSongRecommendations());
        recommendationMenuState.setMovieRecommendation(outputData.getMovieRecommendations());
    }

    @Override
    public void prepareFailView(String error) {
        //todo
    }

    @Override
    public void switchToRecommendationMenu() {
        //todo
    }
}

