package interface_adapter;

import interface_adapter.home_menu.HomeMenuViewModel;
import interface_adapter.ViewManagerModel;
import use_case.back.BackOutputBoundary;

public class BackPresenter implements BackOutputBoundary{

    private final ViewManagerModel viewManagerModel;
    private final NewDocumentViewModel newDocumentViewModel;

    public BackPresenter(ViewManagerModel viewManagerModel, NewDocumentViewModel newDocumentViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.homeMenuViewModel = homeMenuViewModel;
    }
    @Override
    public void prepareSuccessView() {
        viewManagerModel.setState(newDocumentViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
