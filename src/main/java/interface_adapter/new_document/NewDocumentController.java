package interface_adapter.new_document;

import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;
import use_case.go_back.GoBackInputBoundary;

public class NewDocumentController {

    private final GetRecommendationsInputBoundary getRecommendationsInteractor;
    private final GoBackInputBoundary goBackInteractor;


    public NewDocumentController(GetRecommendationsInputBoundary getRecommendationsInteractor, GoBackInputBoundary goBackInteractor) {
        this.getRecommendationsInteractor = getRecommendationsInteractor;
        this.goBackInteractor = goBackInteractor;
    }


    public void executeSave(String title, String date, String textBody) {
        // TODO: Connect with Save Document Use Case Interactor
        System.out.println("Save clicked - Title: " + title + ", Date: " + date);
    }

    public void executeBack() {
        goBackInteractor.execute();
    }

    public void executeGetRecommendations(String textBody) {
        final GetRecommendationsInputData inputData = new GetRecommendationsInputData(textBody);
        getRecommendationsInteractor.execute(inputData);
    }
}
