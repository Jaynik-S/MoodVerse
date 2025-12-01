package interface_adapter.new_document;

import interface_adapter.ViewManagerModel;
import use_case.analyze_keywords.AnalyzeKeywordsOutputBoundary;
import use_case.analyze_keywords.AnalyzeKeywordsOutputData;
import use_case.create_entry.CreateEntryOutputBoundary;
import use_case.create_entry.CreateEntryOutputData;
import use_case.load_entry.LoadEntryOutputBoundary;
import use_case.load_entry.LoadEntryOutputData;
import use_case.save_entry.SaveEntryOutputBoundary;
import use_case.save_entry.SaveEntryOutputData;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class NewDocumentPresenter implements
        SaveEntryOutputBoundary,
        LoadEntryOutputBoundary,
        CreateEntryOutputBoundary,
        AnalyzeKeywordsOutputBoundary{

    private final NewDocumentViewModel newDocumentViewModel;
    private final ViewManagerModel viewManagerModel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy h:mm a");

    public NewDocumentPresenter(NewDocumentViewModel newDocumentViewModel, ViewManagerModel viewManagerModel){
        this.newDocumentViewModel = newDocumentViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CreateEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setTitle(outputData.getTitle());
        state.setTextBody(outputData.getText());
        state.setKeywords(List.of());

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
        state.setKeywords(List.of());

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
    public void prepareSuccessView(SaveEntryOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setSuccessMessage("Document saved successfully");

        state.setTitle(outputData.getTitle());
        state.setTextBody(outputData.getText());

        if (outputData.getDate() != null){
            state.setDate(outputData.getDate().format(formatter));
        }

        state.setError(null);

        newDocumentViewModel.setState(state);
        newDocumentViewModel.firePropertyChanged();
    }

    @Override
    public void prepareSuccessView(AnalyzeKeywordsOutputData outputData) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setKeywords(outputData.getKeywords());
        state.setError(null);
        state.setSuccessMessage(null);

        newDocumentViewModel.setState(state);
        newDocumentViewModel.firePropertyChanged("keywords");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final NewDocumentState state = newDocumentViewModel.getState();
        state.setError(errorMessage);
        newDocumentViewModel.firePropertyChanged();
    }

}
