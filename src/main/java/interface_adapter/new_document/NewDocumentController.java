package interface_adapter.new_document;

import use_case.get_recommendations.GetRecommendationsInputBoundary;
import use_case.get_recommendations.GetRecommendationsInputData;
import use_case.go_back.GoBackInputBoundary;
import use_case.save_entry.SaveEntryInputBoundary;
import use_case.save_entry.SaveEntryInputData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewDocumentController {

    private final GetRecommendationsInputBoundary getRecommendationsInteractor;
    private final GoBackInputBoundary goBackInteractor;
    private final SaveEntryInputBoundary saveEntryInteractor;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NewDocumentController(GetRecommendationsInputBoundary getRecommendationsInteractor, GoBackInputBoundary goBackInteractor, SaveEntryInputBoundary saveEntryInteractor) {
        this.getRecommendationsInteractor = getRecommendationsInteractor;
        this.goBackInteractor = goBackInteractor;
        this.saveEntryInteractor = saveEntryInteractor;
    }


    public void executeSave(String title, String dateString, String textBody) {
       LocalDateTime date;
        if (dateString == null || dateString.isEmpty()) {
            date = LocalDateTime.now();
        } else {
            try {
                date = LocalDateTime.parse(dateString, formatter);
            } catch (Exception e) {
                date = LocalDateTime.now();
            }
        }

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
