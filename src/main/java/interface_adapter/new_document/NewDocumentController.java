package interface_adapter.new_document;

import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;
import use_case.go_back.GoBackInputBoundary;
import use_case.save_entry.SaveEntryInputBoundary;
import use_case.save_entry.SaveEntryInputData;

public class NewDocumentController {

    private final GetRecommendationsInputBoundary getRecommendationsInteractor;
    private final GoBackInputBoundary goBackInteractor;
    private final SaveEntryInputBoundary saveEntryInteractor;


    public NewDocumentController(GetRecommendationsInputBoundary getRecommendationsInteractor, GoBackInputBoundary goBackInteractor, SaveEntryInputBoundary saveEntryInteractor) {
        this.getRecommendationsInteractor = getRecommendationsInteractor;
        this.goBackInteractor = goBackInteractor;
        this.saveEntryInteractor = saveEntryInteractor;
    }


    public void executeSave(String title, String date, String textBody) {
        final SaveEntryInputData inputData = new SaveEntryInputData(title, date, textBody);
        saveEntryInteractor.execute(inputData);
    }

    public void executeBack() {
        goBackInteractor.execute();
    }

    public void executeGetRecommendations(String textBody) {
        final GetRecommendationsInputData inputData = new GetRecommendationsInputData(textBody);
        getRecommendationsInteractor.execute(inputData);
    }
}
