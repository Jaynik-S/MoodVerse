package interface_adapter.z_old_note;

import use_case.z_old_note.NoteInputBoundary;

/**
 * Controller for our Note related Use Cases.
 */
public class NoteController {

    private final NoteInputBoundary noteInteractor;

    public NoteController(NoteInputBoundary noteInteractor) {
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
