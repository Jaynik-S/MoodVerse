package interface_adapter.new_document;

import interface_adapter.ViewModel;
import use_case.save_entry.SaveEntryOutputBoundary;
import use_case.save_entry.SaveEntryOutputData;


/**
 * The Presenter for the New Document Use Cases.
 */
public class NewDocumentPresenter implements SaveEntryOutputBoundary{

    private final NewDocumentViewModel newDocumentViewModel;

    public NewDocumentPresenter(NewDocumentViewModel newDocumentViewModel){
        this.newDocumentViewModel = newDocumentViewModel;
    }

    /**
     * Prepares the success view for the Save Document Use Case.
     * @param outputData the output from a save
     */
    public void prepareSaveSuccessView(SaveEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setError(null);
        newDocumentViewModel.firePropertyChanged();
    }

    /**
     * Prepares the failure view for the New Document related Use Cases.
     * @param errorMessage the explanation of the failure
     */
    public void prepareFailView(String errorMessage) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setError(errorMessage);
        newDocumentViewModel.firePropertyChanged();
    }

}
