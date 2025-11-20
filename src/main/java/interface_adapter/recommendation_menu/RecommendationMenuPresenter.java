package interface_adapter.recommendation_menu;

import interface_adapter.ViewManagerModel;
import interface_adapter.home_menu.HomeMenuViewModel;
import use_case.get_recommendations.GetRecommendationsOutputBoundary;
import use_case.get_recommendations.GetRecommendationsOutputData;

import java.util.ArrayList;

public class RecommendationMenuPresenter implements GetRecommendationsOutputBoundary {

    private final RecommendationMenuViewModel recommendationMenuViewModel;

    private final HomeMenuViewModel homeMenuViewModel;

    private final ViewManagerModel viewManagerModel;

    public RecommendationMenuPresenter(RecommendationMenuViewModel recommendationMenuViewModel,
                                       HomeMenuViewModel homeMenuViewModel,
                                       ViewManagerModel viewManagerModel) {
        this.recommendationMenuViewModel = recommendationMenuViewModel;
        this.homeMenuViewModel = homeMenuViewModel;
        this.viewManagerModel = viewManagerModel;
    }


    @Override
    public void prepareSuccessView(GetRecommendationsOutputData outputData) {
        final RecommendationMenuState recommendationMenuState = recommendationMenuViewModel.getState();
        recommendationMenuState.setSongRecommendation(outputData.getSongRecommendations());
        recommendationMenuState.setMovieRecommendation(outputData.getMovieRecommendations());
        recommendationMenuViewModel.firePropertyChanged();
        recommendationMenuViewModel.setSongRecommendation(outputData.getSongRecommendations());
        recommendationMenuViewModel.setMovieRecommendation(outputData.getMovieRecommendations());
    }

    @Override
    public void prepareFailView(String error) {
        final RecommendationMenuState recommendationMenuState = recommendationMenuViewModel.getState();
        recommendationMenuState.setError(error);
        recommendationMenuViewModel.setError(error);
        recommendationMenuViewModel.firePropertyChanged();
    }

    @Override
    public void switchToRecommendationMenu() {
        viewManagerModel.setState(homeMenuViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}

