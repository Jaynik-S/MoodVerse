package interface_adapter.new_document;

/**
 * The Presenter for the New Document Use Cases.
 */
public class NewDocumentPresenter {

    private final NewDocumentViewModel newDocumentViewModel;

    public NewDocumentPresenter(NewDocumentViewModel newDocumentViewModel) {
        this.newDocumentViewModel = newDocumentViewModel;
    }

    /**
     * Prepares the success view for the Save Document Use Case.
     * @param title the title of the saved document
     * @param date the date of the saved document
     * @param textBody the text body of the saved document
     */
    public void prepareSaveSuccessView(String title, String date, String textBody) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setTitle(title);
        state.setDate(date);
        state.setTextBody(textBody);
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

    /**
     * Prepares the success view for the Get Recommendations Use Case.
     */
    public void prepareRecommendationsSuccessView() {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setError(null);
        newDocumentViewModel.firePropertyChanged();
    }
}
