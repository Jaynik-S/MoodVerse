package interface_adapter.recommendation;

import use_case.note.NoteInputBoundary;

/**
 * Controller for our Note related Use Cases.
 */
public class RecommendationController {

    private final RecommendationInputBoundary recommendationInteractor;

    public RecommendationController(RecommendationInputBoundary recommendationInteractor) {
        this.recommendationInteractor = recommendationInteractor;
    }

    /**
     * Executes the Note related Use Cases.
     * @param note the note to be recorded
     */
    public void execute(String note) {
        if (note != null) {
            recommendationInteractor.executeSave(note);
        }
        else {
            recommendationInteractor.executeRefresh();
        }
    }
}
