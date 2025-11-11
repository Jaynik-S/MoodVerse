package interface_adapter.recommendation;

import use_case.note.NoteInputBoundary;

public class RecommendationController {

    private final NoteInputBoundary noteInteractor;
    
    // todo: add input boundary for recommendation.

    public RecommendationController(NoteInputBoundary noteInteractor) {
        this.noteInteractor = noteInteractor;
    }

    /**
     * Executes the Note related Use Cases.
     * @param note the note to be recorded
     */
    public void execute(String note) {
        if (note != null) {
            noteInteractor.executeSave(note);
        }
        else {
            noteInteractor.executeRefresh();
        }
    }
}
