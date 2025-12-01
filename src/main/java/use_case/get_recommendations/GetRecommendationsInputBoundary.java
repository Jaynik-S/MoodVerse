package use_case.get_recommendations;

public interface GetRecommendationsInputBoundary {
    void execute(GetRecommendationsInputData inputData);

    void switchToRecommendationMenu();

}

