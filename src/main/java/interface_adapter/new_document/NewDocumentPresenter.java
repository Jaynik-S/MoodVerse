package interface_adapter.new_document;

import interface_adapter.ViewManagerModel;
import use_case.create_entry.CreateEntryOutputBoundary;
import use_case.create_entry.CreateEntryOutputData;
import use_case.load_entry.LoadEntryOutputBoundary;
import use_case.load_entry.LoadEntryOutputData;
import use_case.save_entry.SaveEntryOutputBoundary;
import use_case.save_entry.SaveEntryOutputData;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NewDocumentPresenter implements
        SaveEntryOutputBoundary,
        LoadEntryOutputBoundary,
        CreateEntryOutputBoundary{

    private final NewDocumentViewModel newDocumentViewModel;
    private final ViewManagerModel viewManagerModel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public NewDocumentPresenter(NewDocumentViewModel newDocumentViewModel, ViewManagerModel viewManagerModel){
        this.newDocumentViewModel = newDocumentViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CreateEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setTitle(outputData.getTitle());
        state.setTextBody(outputData.getText());

        if (outputData.getDate() != null) {
            state.setDate(outputData.getDate().format(formatter));
        } else {
            state.setDate("");
        }

        state.setError(null);
        state.setSuccessMessage(null);

        newDocumentViewModel.setState(state);
        newDocumentViewModel.firePropertyChanged();

        viewManagerModel.setState(newDocumentViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView(LoadEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setTitle(outputData.getTitle());
        state.setTextBody(outputData.getText());

        if (outputData.getDate() != null) {
            state.setDate(outputData.getDate().format(formatter));
        }

        state.setError(null);
        state.setSuccessMessage(null);

        newDocumentViewModel.setState(state);
        newDocumentViewModel.firePropertyChanged();

        viewManagerModel.setState(newDocumentViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView(SaveEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setSuccessMessage("Document saved successfully");

        if (outputData.getDate() != null){
            state.setDate(outputData.getDate().format(formatter));
        }

        state.setError(null);

        newDocumentViewModel.setState(state);
        newDocumentViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setError(errorMessage);
        newDocumentViewModel.firePropertyChanged();
    }

}
