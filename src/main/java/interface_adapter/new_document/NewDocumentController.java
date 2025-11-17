package interface_adapter.new_document;

import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;

public class NewDocumentController {

    private final GetRecommendationsInputBoundary getRecommendationsInteractor;

    public NewDocumentController(GetRecommendationsInputBoundary getRecommendationsInteractor) {
        this.getRecommendationsInteractor = getRecommendationsInteractor;
    }


    public void executeSave(String title, String date, String textBody) {
        // TODO: Connect with Save Document Use Case Interactor
        System.out.println("Save clicked - Title: " + title + ", Date: " + date);
    }

    public void executeBack() {
        // TODO: Connect with Back Use Case Interactor
        System.out.println("Back clicked");
    }

    public void executeGetRecommendations(String textBody) {
        final GetRecommendationsInputData inputData = new GetRecommendationsInputData(textBody);
        getRecommendationsInteractor.execute(inputData);
    }
}
