package interface_adapter.recommendation_menu;

import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;

public class RecommendationMenuController {
    private final GetRecommendationsInputBoundary getRecommendationInteractor;

    public RecommendationMenuController(GetRecommendationsInputBoundary getRecommendationInteractor) {
        this.getRecommendationInteractor = getRecommendationInteractor;
    }

    public void execute(GetRecommendationsInputData inputData) {
        getRecommendationInteractor.execute(inputData);
    }

    /**
     * Executes the "switchToRecommendationMenu" Use Case.
     */
    void switchToRecommendationMenu() {
        getRecommendationInteractor.switchToRecommendationMenu();
    }

}

