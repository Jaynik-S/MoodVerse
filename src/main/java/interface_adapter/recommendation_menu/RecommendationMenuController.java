package interface_adapter.recommendation_menu;

import use_case.back.BackInteractor;
import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;

public class RecommendationMenuController {
    private final GetRecommendationsInputBoundary getRecommendationInteractor;

    private final BackInteractor backInteractor;

    public RecommendationMenuController(GetRecommendationsInputBoundary getRecommendationInteractor, BackInteractor backInteractor) {
        this.getRecommendationInteractor = getRecommendationInteractor;
        this.backInteractor = backInteractor;
    }

    public void execute(GetRecommendationsInputData inputData) {
        getRecommendationInteractor.execute(inputData);
    }


    public void executeBack() {
        backInteractor.execute();
    }
    /**
     * Executes the "switchToRecommendationMenu" Use Case.
     */
    void switchToRecommendationMenu() {
        getRecommendationInteractor.switchToRecommendationMenu();
    }

}

